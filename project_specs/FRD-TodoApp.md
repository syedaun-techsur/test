# Functional Requirements Document — TodoApp

**Document Version:** 1.0  
**Project:** TodoApp  
**Based on:** PRD-TodoApp.md  
**Last Updated:** 2026-04-27  
**Status:** Initial Draft

---

## 1. Overview

This document provides detailed functional specifications for the TodoApp, a lightweight task management web application. The application uses vanilla HTML/CSS/JavaScript with browser LocalStorage for data persistence. All specifications are designed to enable developers to implement each feature without ambiguity.

---

## 2. Feature Specifications

### F0: Task Creation

**Description:** This feature enables users to add new tasks to their todo list by entering a title. The task is immediately created and appears in the list upon submission. This is the primary entry point for adding content to the application.

**Terminology:**
- **Task Title:** The text description of a task item
- **Submission:** The action of adding a task to the list
- **Input Field:** The text box where users type task titles

**Sub-features:**
- Single-line text input for task title
- Submit via Enter key press
- Submit via click on Add button
- Auto-clear input after successful submission
- Prevent empty/whitespace-only submissions

**Process:**
1. User focuses on task input field
2. User types task title (1-500 characters)
3. User presses Enter key or clicks Add button
4. System validates input is not empty or whitespace-only
5. System creates new task object with unique ID and completed=false
6. System adds task to task list array
7. System saves updated list to LocalStorage
8. System renders new task in the list
9. System clears input field and maintains focus for quick next entry

**Inputs:**
- `title` (string, required): Task title text from 1 to 500 characters
- `submitMethod` (string, enum): "enter" or "buttonClick"

**Outputs:**
- New task object added to in-memory task list
- Task displayed in UI with unchecked checkbox
- Input field cleared
- LocalStorage updated with new task array

**Validation:**
- Title must not be empty after trimming whitespace
- Title must not exceed 500 characters
- Title must be a string type (sanitize to string if different)
- Leading/trailing whitespace should be trimmed before storage

**Error States:**
| Scenario | Handling | User Feedback |
|----------|----------|---------------|
| Empty title submitted | Reject submission | Input field remains, no change to list |
| Whitespace-only title | Reject submission | Input field remains, no change to list |
| Title exceeds 500 chars | Truncate to 500 characters | Task created with truncated title |
| LocalStorage quota full | Show error message | "Storage full. Please delete some tasks." |
| LocalStorage unavailable | Show read-only mode | "Storage unavailable. Tasks cannot be saved." |

---

### F1: Task Completion Toggle

**Description:** This feature allows users to mark tasks as complete or incomplete by clicking a checkbox. Completed tasks receive visual distinction (strikethrough, dimmed color) to indicate their status. The completion state persists across browser sessions.

**Terminology:**
- **Completed State:** Task marked as done (checkbox checked)
- **Active State:** Task not yet completed (checkbox unchecked)
- **Toggle:** Action of switching between completed and active states

**Sub-features:**
- Checkbox control for each task item
- Visual strikethrough on completed task titles
- Dimmed opacity on completed tasks
- Instant visual feedback on click
- State persists in LocalStorage

**Process:**
1. User clicks checkbox next to a task
2. System toggles completed status (true → false or false → true)
3. System updates task object in task list array
4. System saves updated list to LocalStorage
5. System updates visual styling (add/remove strikethrough class)
6. System updates task counter if F6 is implemented

**Inputs:**
- `taskId` (string, required): Unique identifier of the task
- `currentStatus` (boolean): Current completion status before toggle

**Outputs:**
- Task's completed status inverted
- Visual class applied/removed on task element
- LocalStorage updated

**Validation:**
- Task ID must exist in task list
- Status must be boolean type

**Error States:**
| Scenario | Handling | User Feedback |
|----------|----------|---------------|
| Invalid task ID | Ignore click | No change to UI |
| LocalStorage write fails | Revert in-memory state | "Could not save. Try again." |

---

### F2: Task Editing

**Description:** This feature enables users to modify the title of existing tasks inline without opening dialogs or modal windows. Users enter edit mode via double-click or edit button, modify the text, and save or cancel their changes.

**Terminology:**
- **Edit Mode:** State where task title is replaceable with text input
- **View Mode:** Default display state showing static task title
- **Save:** Confirm changes and update task title
- **Cancel:** Discard changes and revert to original title

**Sub-features:**
- Double-click on task title enters edit mode
- Edit button/icon alternative to enter edit mode
- Inline text input replaces title display
- Save on Enter key press
- Save on clicking outside (blur)
- Cancel on Escape key press
- Empty titles revert to previous value

**Process:**
1. User double-clicks task title or clicks edit button
2. System switches task to edit mode (input field replaces text)
3. User modifies title text
4. User presses Enter or clicks outside (save):
   a. System validates title is not empty
   b. System updates task title in task list
   c. System saves to LocalStorage
   d. System switches to view mode with new title
5. OR User presses Escape (cancel):
   a. System discards changes
   b. System switches to view mode with original title

**Inputs:**
- `taskId` (string, required): Unique identifier of task to edit
- `newTitle` (string): New title text from input field
- `saveMethod` (string, enum): "enter" | "blur" | "escape"

**Outputs:**
- Task title updated in task list and LocalStorage
- UI switches from input to text display
- Task counter updates if applicable

**Validation:**
- New title must not be empty after trimming
- New title must not exceed 500 characters
- Empty title rejected → revert to original

**Error States:**
| Scenario | Handling | User Feedback |
|----------|----------|---------------|
| Empty title on save | Reject change | Revert to original title |
| Whitespace-only title | Reject change | Revert to original title |
| Escape pressed | Cancel edit | Original title restored |
| LocalStorage unavailable | Allow edit in memory | Changes may be lost on refresh |

---

### F3: Task Deletion

**Description:** This feature allows users to remove tasks from the list entirely. Each task displays a delete button that immediately removes the task upon click. No confirmation dialog is shown to maintain speed and simplicity.

**Terminology:**
- **Delete:** Action of permanently removing a task
- **Delete Button:** UI element that triggers task removal

**Sub-features:**
- Delete icon/button visible on each task
- Click triggers immediate removal
- No confirmation dialog (speed over safety)
- Deleted tasks are not recoverable

**Process:**
1. User clicks delete button on a task
2. System finds task by ID in task list
3. System removes task from task list array
4. System saves updated list to LocalStorage
5. System removes task element from DOM

**Inputs:**
- `taskId` (string, required): Unique identifier of task to delete

**Outputs:**
- Task removed from task list
- Task element removed from UI
- LocalStorage updated

**Validation:**
- Task ID must exist in task list

**Error States:**
| Scenario | Handling | User Feedback |
|----------|----------|---------------|
| Invalid task ID | Ignore action | No UI change |
| LocalStorage write fails | Keep in memory | "Could not delete. Try again." |

---

### F4: Data Persistence

**Description:** This feature ensures all task data is stored in browser LocalStorage, making it survive page refreshes and browser sessions. Every change to tasks (add, edit, delete, toggle) triggers an auto-save to LocalStorage. The application loads saved tasks on startup.

**Terminology:**
- **LocalStorage:** Browser's key-value storage API
- **Auto-save:** Automatic save triggered by any data change
- **Data Corruption:** Invalid data that cannot be parsed

**Sub-features:**
- Auto-save on every task modification
- Load tasks from LocalStorage on page initialization
- Handle storage quota exceeded gracefully
- Handle corrupted data gracefully (reset to empty)

**Process:**
1. Page loads → System checks LocalStorage for existing tasks
2. System attempts to parse stored JSON
3. If valid: Load tasks into memory and render
4. If invalid/corrupted: Reset to empty array, log warning
5. On any task change (add/edit/delete/toggle):
   a. Serialize task array to JSON
   b. Write to LocalStorage key "todo_tasks"
   c. If write fails (quota): Show warning message

**Inputs:**
- `tasks` (array): Full array of task objects

**Outputs:**
- JSON string stored in LocalStorage key "todo_tasks"
- Task array loaded into memory on page load

**Validation:**
- Data must be valid JSON array
- Each task must have id (string), title (string), completed (boolean)
- Unknown fields in stored data should be ignored

**Error States:**
| Scenario | Handling | User Feedback |
|----------|----------|---------------|
| LocalStorage not available | Show read-only mode | "Storage unavailable. Changes will not be saved." |
| Quota exceeded | Show warning | "Storage full. Some changes may not be saved." |
| Corrupted JSON | Reset to empty array | (Silent - user sees empty list) |
| JSON parse error | Reset to empty array | (Silent - log error to console) |

---

### F5: Clear Completed Tasks

**Description:** This feature provides a bulk delete action that removes all completed tasks from the list in a single operation. The button is only visible when there is at least one completed task, keeping the interface clean.

**Terminology:**
- **Completed Tasks:** Tasks where completed=true
- **Clear Action:** Button that triggers bulk deletion

**Sub-features:**
- Button visible only when completed tasks exist
- Single action removes all completed items
- Active tasks remain untouched
- Button labeled "Clear completed" or similar

**Process:**
1. System monitors task list for completed count
2. When completed count > 0: Show Clear completed button
3. When completed count = 0: Hide Clear completed button
4. User clicks button:
   a. System filters out completed tasks from array
   b. System saves updated list to LocalStorage
   c. System removes completed task elements from DOM

**Inputs:**
- None (triggered by button click)

**Outputs:**
- Array with only active tasks (completed=false)
- LocalStorage updated
- Button hidden when no completed tasks remain

**Validation:**
- Confirm at least one completed task exists before action

**Error States:**
| Scenario | Handling | User Feedback |
|----------|----------|---------------|
| LocalStorage write fails | Revert in memory | "Could not clear. Try again." |

---

### F6: Task Counter

**Description:** This feature displays a count of remaining (incomplete) tasks to give users a quick overview of how much is left to do. The counter updates dynamically as tasks are completed or added.

**Terminology:**
- **Active Count:** Number of tasks where completed=false
- **Counter Display:** UI element showing active task count

**Sub-features:**
- Display "X items left" text
- Update dynamically on any task change
- Positioned in footer or header area
- Singular/plural handling ("1 item left" vs "2 items left")

**Process:**
1. On initial load: Calculate active count, display in counter
2. On any task change:
   a. Recalculate active count (filter completed=false)
   b. Update counter text
   c. Handle singular/plural grammar

**Inputs:**
- None (calculated from task list)

**Outputs:**
- Text string: "X items left" or "X item left"

**Validation:**
- Count must be non-negative integer

**Error States:**
- None (display only, no error states)

---

## 3. Technical Specifications

### 3.1 API Design (JavaScript Module Interface)

Since this is a client-side application with no HTTP server, the "API" consists of JavaScript functions and module interfaces.

#### Task Module API

```javascript
// TaskOperations - Core task management functions

/**
 * Create a new task
 * @param {string} title - Task title (1-500 chars)
 * @returns {object} Created task object or null if failed
 */
TaskOperations.create(title);

/**
 * Get all tasks
 * @returns {array} Array of task objects
 */
TaskOperations.getAll();

/**
 * Update an existing task
 * @param {string} id - Task ID
 * @param {object} updates - Fields to update (title, completed)
 * @returns {boolean} Success status
 */
TaskOperations.update(id, updates);

/**
 * Delete a task
 * @param {string} id - Task ID
 * @returns {boolean} Success status
 */
TaskOperations.delete(id);

/**
 * Toggle task completion status
 * @param {string} id - Task ID
 * @returns {boolean} New completion status or null if failed
 */
TaskOperations.toggleComplete(id);

/**
 * Clear all completed tasks
 * @returns {number} Number of tasks deleted
 */
TaskOperations.clearCompleted();
```

#### Storage Module API

```javascript
// StorageService - LocalStorage abstraction

/**
 * Save tasks to LocalStorage
 * @param {array} tasks - Array of task objects
 * @returns {boolean} Success status
 */
StorageService.save(tasks);

/**
 * Load tasks from LocalStorage
 * @returns {array} Array of task objects or empty array
 */
StorageService.load();

/**
 * Check if storage is available
 * @returns {boolean} Storage availability
 */
StorageService.isAvailable();

/**
 * Check available storage space
 * @returns {number} Available bytes or -1 if unknown
 */
StorageService.getAvailableSpace();
```

#### Render Module API

```javascript
// RenderService - UI rendering functions

/**
 * Render all tasks to DOM
 * @param {array} tasks - Array of task objects
 */
RenderService.renderAll(tasks);

/**
 * Add single task to DOM
 * @param {object} task - Task object
 */
RenderService.renderTask(task);

/**
 * Remove task from DOM
 * @param {string} id - Task ID
 */
RenderService.removeTask(id);

/**
 * Update task display (toggle/complete status)
 * @param {string} id - Task ID
 * @param {boolean} completed - Completion status
 */
RenderService.updateTaskStatus(id, completed);

/**
 * Update counter display
 * @param {number} count - Active task count
 */
RenderService.updateCounter(count);

/**
 * Show/hide clear completed button
 * @param {boolean} visible - Visibility state
 */
RenderService.toggleClearButton(visible);
```

### 3.2 Database Schema (LocalStorage)

The application uses a single LocalStorage key with JSON-serialized data.

#### Storage Key

```
Key: "todo_tasks"
Value: JSON string
```

#### Data Schema

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "array",
  "items": {
    "type": "object",
    "properties": {
      "id": {
        "type": "string",
        "description": "Unique identifier (UUID or timestamp-based)"
      },
      "title": {
        "type": "string",
        "description": "Task title text",
        "minLength": 1,
        "maxLength": 500
      },
      "completed": {
        "type": "boolean",
        "description": "Completion status"
      },
      "createdAt": {
        "type": "string",
        "format": "date-time",
        "description": "ISO 8601 timestamp when created"
      },
      "updatedAt": {
        "type": "string",
        "format": "date-time",
        "description": "ISO 8601 timestamp when last modified"
      }
    },
    "required": ["id", "title", "completed"]
  }
}
```

#### Example Data

```json
[
  {
    "id": "1745750400123-abc123",
    "title": "Buy groceries",
    "completed": false,
    "createdAt": "2026-04-27T10:00:00.123Z",
    "updatedAt": "2026-04-27T10:00:00.123Z"
  },
  {
    "id": "1745750400456-def456",
    "title": "Complete project report",
    "completed": true,
    "createdAt": "2026-04-27T09:30:00.456Z",
    "updatedAt": "2026-04-27T11:15:00.789Z"
  }
]
```

### 3.3 Error Handling

#### Error Categories

| Category | Detection Method | User Feedback | Recovery |
|----------|------------------|---------------|----------|
| Storage Unavailable | `StorageService.isAvailable()` | Show banner "Storage unavailable" | Read-only mode |
| Storage Full | Catch QUOTA_EXCEEDED_ERR | Show "Storage full" message | Suggest deleting tasks |
| Data Corruption | JSON parse failure | Silent reset | Start with empty list |
| XSS in Task Title | Input sanitization | Strip HTML/script tags | Sanitized title stored |

#### Global Error Handler

```javascript
// Error handling configuration
const ErrorConfig = {
  onStorageUnavailable: () => {
    document.body.classList.add('storage-unavailable');
    // Show warning banner
  },
  onStorageFull: () => {
    alert('Storage full. Please delete some tasks to continue.');
  },
  onDataCorruption: () => {
    console.warn('Corrupted data reset to empty');
    // Silent recovery
  }
};
```

### 3.4 Security

#### Input Sanitization

All task titles must be sanitized to prevent XSS attacks:

```javascript
/**
 * Sanitize task title to prevent XSS
 * @param {string} title - Raw input
 * @returns {string} Sanitized string
 */
function sanitizeTitle(title) {
  // Convert to string if not
  let sanitized = String(title);
  
  // Remove HTML tags
  sanitized = sanitized.replace(/<[^>]*>/g, '');
  
  // Trim whitespace
  sanitized = sanitized.trim();
  
  // Encode special HTML characters
  const div = document.createElement('div');
  div.textContent = sanitized;
  sanitized = div.innerHTML;
  
  return sanitized;
}
```

#### Security Constraints

- No eval() or new Function() with user input
- No innerHTML with raw user data (use textContent)
- No external network requests
- No sensitive data storage

---

## 4. Integration Points

### 4.1 Browser APIs Used

| API | Purpose | Fallback |
|-----|---------|----------|
| LocalStorage | Persistent data storage | Show read-only warning |
| JSON.parse/stringify | Data serialization | Reset on parse error |

### 4.2 DOM Events

| Event | Handler | Action |
|-------|---------|--------|
| Enter key in input | `handleKeyDown` | Submit new task |
| Click on add button | `handleAddClick` | Submit new task |
| Click on checkbox | `handleToggle` | Toggle completion |
| Double-click on title | `handleEditStart` | Enter edit mode |
| Enter key in edit input | `handleEditSave` | Save edit |
| Escape key in edit input | `handleEditCancel` | Cancel edit |
| Blur on edit input | `handleEditSave` | Save edit on blur |
| Click on delete button | `handleDelete` | Delete task |
| Click on clear button | `handleClearCompleted` | Clear all completed |

### 4.3 Initialization Flow

```javascript
// Application initialization
function init() {
  // 1. Check storage availability
  if (!StorageService.isAvailable()) {
    ErrorConfig.onStorageUnavailable();
  }
  
  // 2. Load tasks from storage
  let tasks = [];
  try {
    tasks = StorageService.load();
  } catch (e) {
    ErrorConfig.onDataCorruption();
    tasks = [];
  }
  
  // 3. Validate loaded data
  tasks = validateTasks(tasks);
  
  // 4. Render initial state
  RenderService.renderAll(tasks);
  RenderService.updateCounter(countActive(tasks));
  RenderService.toggleClearButton(hasCompleted(tasks));
  
  // 5. Attach event listeners
  attachEventListeners();
}
```

---

## 5. Validation Rules Summary

### Task Title Validation

| Rule | Constraint | Error Handling |
|------|------------|----------------|
| Not empty | title.trim().length > 0 | Reject submission |
| Not whitespace | title.trim().length > 0 | Reject submission |
| Max length | title.length <= 500 | Truncate to 500 |
| Type check | typeof title === 'string' | Convert to string |

### LocalStorage Operations

| Rule | Constraint | Error Handling |
|------|------------|----------------|
| Write success | localStorage.setItem succeeds | Show error if fails |
| Read valid JSON | JSON.parse succeeds | Reset to empty |
| Storage available | localStorage defined | Show warning |

---

## 6. Out of Scope

The following are explicitly excluded from this FRD:

- Backend API endpoints
- User authentication
- Multiple task lists/projects
- Due dates and time-based features
- Priority levels or tags
- Collaboration features
- Cloud sync
- Export/import functionality
- Drag-and-drop reordering
- Recurring tasks

---

## 7. Appendix

### A. Data Flow Diagram

```
User Input → Validation → Task Operations → Storage Service → LocalStorage
                                     ↓
                               Render Service → DOM Update
```

### B. ID Generation

Task IDs are generated using timestamp + random suffix:

```javascript
function generateId() {
  return Date.now().toString() + '-' + Math.random().toString(36).substr(2, 9);
}
```

### C. Browser Compatibility Notes

- LocalStorage available in all modern browsers (IE8+)
- JSON.parse available in IE7+ with polyfill
- Template literals require ES6 (not supported in IE11)

---

*End of FRD — TodoApp*