# Phase 1: Storage Foundation - Research

**Researched:** 2026-04-27
**Domain:** Browser LocalStorage API and task persistence
**Confidence:** HIGH

## Summary

Phase 1 focuses on building the persistence layer for tasks using browser LocalStorage. This requires wrapping the native `localStorage` API with error handling, JSON serialization, and a clean interface for save/load operations. The key technical decisions involve ID generation (using `crypto.randomUUID()`) and whether to implement cross-tab synchronization via the Storage event listener.

**Primary recommendation:** Implement a Storage module with `saveTasks(tasks)` and `loadTasks()` functions that wrap localStorage with try-catch error handling, handle QuotaExceededError gracefully, and provide a simple Task[] interface for the CRUD phase (Phase 2) to consume.

## Standard Stack

### Core
| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| Native localStorage | Browser API | Key-value persistence | Standard browser API, no dependencies |
| JSON.parse/stringify | Built-in | Serialize/deserialize task objects | Native JavaScript, handles nested objects |
| crypto.randomUUID() | Modern browsers (2022+) | Generate unique task IDs | Built-in, cryptographically secure UUID v4 |

### Supporting
| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| None required | - | - | Minimal v1 scope |

### Alternatives Considered
| Instead of | Could Use | Tradeoff |
|------------|-----------|----------|
| crypto.randomUUID() | `uuid` npm package | Adds dependency for older browser support (IE11) |
| crypto.randomUUID() | Timestamp + random string | Less standard, potential collision |
| Custom polyfill | Crypto API polyfill | Extra code, rarely needed |

**Installation:**
No npm packages required - pure browser JavaScript.

## Architecture Patterns

### Recommended Module Structure
```
src/
├── storage/
│   ├── index.js        # Public API: saveTasks, loadTasks
│   └── storage.js       # Core: localStorage wrapper with error handling
```

### Module: saveTasks(tasks)
**What:** Serializes and saves task array to localStorage
**When to use:** After any task modification (add, edit, delete, toggle)
**Code pattern:**
```javascript
// Source: WebSearch best practices
function saveTasks(tasks) {
  try {
    localStorage.setItem('todo-app-tasks', JSON.stringify(tasks));
    return { success: true };
  } catch (error) {
    if (error.name === 'QuotaExceededError' || error.code === 22) {
      return { success: false, error: 'storage_full' };
    }
    return { success: false, error: 'unknown' };
  }
}
```

### Module: loadTasks()
**What:** Loads and deserializes tasks from localStorage
**When to use:** On app initialization
**Code pattern:**
```javascript
// Source: WebSearch best practices
function loadTasks() {
  try {
    const data = localStorage.getItem('todo-app-tasks');
    return data ? JSON.parse(data) : [];
  } catch (error) {
    console.error('Error loading tasks:', error);
    return [];
  }
}
```

### Module: generateTaskId()
**What:** Creates unique identifier for each task
**When to use:** When creating new task
**Code pattern:**
```javascript
// Source: MDN Web Docs - crypto.randomUUID()
function generateTaskId() {
  // Requires secure context (HTTPS or localhost)
  // Falls back for insecure contexts
  if (typeof crypto !== 'undefined' && crypto.randomUUID) {
    return crypto.randomUUID();
  }
  // Fallback: timestamp + random (simpler, works in dev)
  return Date.now().toString(36) + Math.random().toString(36).substr(2);
}
```

### Cross-Tab Sync (Optional)
**What:** Uses Storage event to detect changes in other tabs
**When to use:** If user has multiple tabs open
**Code pattern:**
```javascript
// Source: MDN Web APIs - Window:storage event
window.addEventListener('storage', (event) => {
  if (event.key === 'todo-app-tasks' && event.newValue) {
    // Update UI with new value from other tab
    const tasks = JSON.parse(event.newValue);
    renderTasks(tasks);
  }
});
```
**Important notes:**
- Storage event only fires in OTHER tabs, not the originating tab
- Safari does NOT reliably fire storage events between tabs
- Requires HTTPS context to work properly
- Recommendation: Defer to Phase 2 or skip for MVP simplicity

### Anti-Patterns to Avoid
- **Direct localStorage calls in components:** Never call localStorage directly in UI code - creates scattered try-catch, hard to maintain. Use a storage module.
- **Assuming localStorage always works:** Can throw in private browsing (Safari), can be full, can be disabled. Always wrap in try-catch.
- **Storing non-JSON-serializable data:** localStorage only stores strings. Never store Dates (serialize to unix timestamp), Functions (won't work), or Objects (must stringify).

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| ID generation | Custom random string algorithm | crypto.randomUUID() | Built-in, cryptographically secure, standardized |
| JSON serialization | Custom CSV or delimiter-separated | JSON.stringify/parse | Handles nested objects, edge cases |
| Error handling | Assume setItem always succeeds | try-catch with QuotaExceededError check | Private browsing, quota limits, disabled storage |

**Key insight:** LocalStorage is deceptively simple but has edge cases (Safari private mode throws, quota limits, disabled storage). Wrap everything in try-catch.

## Common Pitfalls

### Pitfall 1: Not Handling QuotaExceededError
**What goes wrong:** App crashes when localStorage is full (common in Safari, 2.5-5MB limit)
**Why it happens:** Browser storage limit reached (5MB typical, 2.5MB on some Safari)
**How to avoid:** Wrap `setItem` in try-catch, catch `QuotaExceededError` specifically
**Warning signs:** "QuotaExceededError" or error code 22 in caught error

### Pitfall 2: JSON.parse Failure on Corrupted Data
**What goes wrong:** App can't load tasks if localStorage value is corrupted
**Why it happens:** Manual edit in dev tools, partial write, or previous bug
**How to avoid:** Wrap JSON.parse in try-catch, return empty array on error
**Warning signs:** SyntaxError when parsing localStorage value

### Pitfall 3: Private/Incognito Mode Storage Unavailable
**What goes wrong:** setItem throws even with small data
**Why it happens:** Safari and some browsers restrict localStorage in private mode
**How to avoid:** Check `isStorageAvailable()` before first use, provide graceful fallback
**Warning signs:** SecurityError or empty quota

### Pitfall 4: Storing Non-Serializable Values
**What goes wrong:** Data disappears or becomes "[object Object]"
**Why it happens:** Forgetting JSON.stringify, storing Date objects directly
**How to avoid:** Always stringify before saving, convert Dates to timestamps

## Code Examples

### Complete Storage Module Pattern
```javascript
// storage.js - Core storage utilities

const STORAGE_KEY = 'todo-app-tasks';

/**
 * Check if localStorage is available
 * @returns {boolean}
 */
export function isStorageAvailable() {
  try {
    const test = '__test__';
    localStorage.setItem(test, test);
    localStorage.removeItem(test);
    return true;
  } catch (e) {
    return false;
  }
}

/**
 * Generate unique task ID
 * @returns {string}
 */
export function generateTaskId() {
  // crypto.randomUUID() requires secure context (HTTPS/localhost)
  if (typeof crypto !== 'undefined' && crypto.randomUUID) {
    return crypto.randomUUID();
  }
  // Fallback for insecure contexts
  return Date.now().toString(36) + '-' + Math.random().toString(36).substr(2, 9);
}

/**
 * Load tasks from localStorage
 * @returns {Array} Array of task objects
 */
export function loadTasks() {
  try {
    const data = localStorage.getItem(STORAGE_KEY);
    if (data === null) return [];
    return JSON.parse(data);
  } catch (error) {
    console.error('Failed to load tasks:', error);
    return [];
  }
}

/**
 * Save tasks to localStorage
 * @param {Array} tasks - Array of task objects
 * @returns {Object} { success: boolean, error?: string }
 */
export function saveTasks(tasks) {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
    return { success: true };
  } catch (error) {
    if (error.name === 'QuotaExceededError' || error.code === 22) {
      return { success: false, error: 'storage_full' };
    }
    console.error('Failed to save tasks:', error);
    return { success: false, error: 'unknown' };
  }
}
```

### Storage Availability Check
```javascript
// Source: Modern Web Development best practices
export function isStorageReady() {
  try {
    const TEST_KEY = '__storage_test__';
    localStorage.setItem(TEST_KEY, '1');
    localStorage.removeItem(TEST_KEY);
    return { available: true };
  } catch (e) {
    // Private browsing or disabled
    return { available: false, reason: 'unavailable' };
  }
}
```

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| Math.random() for IDs | crypto.randomUUID() | 2022 (Chrome 92+) | Cryptographically secure, standardized |
| Custom storage wrappers | Native JSON.parse/stringify | Always best | Handles nested data properly |
| No error handling | try-catch with QuotaExceededError | Current best practice | Works in private browsing |
| Manual timestamp IDs | UUID v4 or v7 | RFC 9562 (2024) | v7 is sortable, but v4 fine for tasks |

**Deprecated/outdated:**
- Internet Explorer support (no longer needs polyfills for basic localStorage)
- Synchronous XHR for persistence (localStorage is simpler for client-only apps)

## Open Questions

1. **Should cross-tab sync be implemented?**
   - What we know: Storage event works in Chrome/Firefox, unreliable in Safari
   - What's unclear: Whether multi-tab users are common for todo apps
   - Recommendation: Defer to Phase 2 or implement toggle, default OFF

2. **Should we check available storage space before saving?**
   - What we know: Can estimate with navigator.storage.estimate() but async
   - What's unclear: Complexity trade-off for v1
   - Recommendation: Skip for v1, let catch block handle full storage

## Sources

### Primary (HIGH confidence)
- MDN Web Docs - Window: storage event (https://developer.mozilla.org/docs/Web/API/Window/onstorage)
- MDN Web Docs - Crypto: randomUUID() method (https://developer.mozilla.org/docs/Web/API/Crypto/randomUUID)
- MDN Web Docs - Storage quotas (https://developer.mozilla.org/docs/Web/API/Storage_API/Storage_quotas_and_eviction_criteria)

### Secondary (MEDIUM confidence)
- BSWEN: Fix QuotaExceededError in localStorage (https://docs.bswen.com/blog/2026-04-07-fix-quotaexceedederror-localstorage) - Published 2026-04-07
- Dev.to: UUID v7, ULID, KSUID comparison (https://dev.to/sendotltd/uuid-v7-ulid-ksuid-whats-the-difference-i-implemented-all-five-46k1) - Published 2026-04-11
- Dev.to: Cross-tab synchronization (https://medium.com/@vinaykumarbr07/cross-tab-state-synchronization-in-react-using-the-browser-storage-event-14b6f1a97ea6) - Published 2026-01-04

### Tertiary (LOW confidence)
- DevKit: Generate UUID in JavaScript (https://devkit.escalixstudio.com/blog/generate-javascript-uuid) - For fallback reference only

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH - Native browser APIs with well-documented behavior
- Architecture: HIGH - Simple module pattern, no complex dependencies
- Pitfalls: HIGH - All documented with solutions from multiple sources

**Research date:** 2026-04-27
**Valid until:** 90 days (stable API, unlikely to change)