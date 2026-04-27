# User Stories — TodoApp

**Document Version:** 1.0  
**Project:** TodoApp  
**Based on:** PRD-TodoApp.md, FRD-TodoApp.md, PERSONAS-TodoApp.md  
**Last Updated:** 2026-04-27

---

## 1. Document Overview

This document captures user needs for the TodoApp as structured user stories with clear acceptance criteria. Stories are organized by feature epic and reference the PRD features they implement. All personas are drawn from PERSONAS-TodoApp.md.

---

## 2. Priority Definitions

| Priority | Meaning | Stories |
|----------|---------|---------|
| **P0** | Critical — Core MVP requirement, must ship | F0, F1, F2, F3, F4 |
| **P1** | High — Important enhancement | (None assigned) |
| **P2** | Medium — Improves list management | F5 |
| **P3** | Low — Nice-to-have, minimal implementation value | F6 |

---

## 3. Epic: Task Creation (F0)

### Epic Description

Users can add new tasks by entering a title. The task immediately appears in the list upon submission. This is the primary entry point for adding content to the application.

---

### US-0.1: Add New Task via Enter Key

**As a** Marcus Chen (Busy Professional), **I want to** add a task by typing the title and pressing Enter, **so that** I can quickly capture tasks during brief work breaks without reaching for the mouse.

**Acceptance Criteria:**

- [ ] Input field accepts text when user focuses on it
- [ ] Pressing Enter creates a new task with the entered title
- [ ] New task appears immediately in the task list below the input
- [ ] Input field clears after successful task creation
- [ ] Input field retains focus for rapid sequential task entry
- [ ] Empty submissions are rejected with no change to the list

**Priority:** P0 | **Feature Ref:** F0

---

### US-0.2: Add New Task via Button Click

**As a** Robert Thompson (Retiree), **I want to** add a task by clicking an Add button, **so that** I have a clear clickable option when I prefer mouse interaction over keyboard shortcuts.

**Acceptance Criteria:**

- [ ] Add button is visible next to or below the input field
- [ ] Clicking the Add button creates a new task with the entered title
- [ ] New task appears immediately in the task list
- [ ] Input field clears after successful task creation
- [ ] Button has clear visual indication it is clickable
- [ ] Empty submissions are rejected with no change to the list

**Priority:** P0 | **Feature Ref:** F0

---

### US-0.3: Prevent Empty Task Submission

**As a** Emma Rodriguez (University Student), **I want to** be prevented from adding empty tasks, **so that** I don't accidentally create blank items that confuse my task list.

**Acceptance Criteria:**

- [ ] Whitespace-only submissions are rejected
- [ ] Input field remains unchanged when empty submission is attempted
- [ ] No error message appears (silent rejection for simplicity)
- [ ] Task list remains unchanged after rejection

**Priority:** P0 | **Feature Ref:** F0

---

### US-0.4: Task Title Character Limit

**As a** Marcus Chen (Busy Professional), **I want to** be prevented from entering titles longer than 500 characters, **so that** my task list remains readable and usable.

**Acceptance Criteria:**

- [ ] Titles exceeding 500 characters are truncated to 500 characters
- [ ] User can enter up to 500 characters without restriction
- [ ] Truncated title is saved and displayed correctly
- [ ] No error message appears when truncation occurs

**Priority:** P0 | **Feature Ref:** F0

---

## 4. Epic: Task Completion Toggle (F1)

### Epic Description

Users can mark tasks as complete or incomplete by clicking a checkbox or toggle. Completed tasks are visually distinguished through strikethrough and color changes. The completion state persists across browser sessions.

---

### US-1.1: Toggle Task Completion via Checkbox

**As a** Emma Rodriguez (University Student), **I want to** click a checkbox to mark tasks complete or incomplete, **so that** I can visually track my progress on coursework and assignments.

**Acceptance Criteria:**

- [ ] Each task displays a checkbox control
- [ ] Clicking an unchecked checkbox marks the task as complete
- [ ] Clicking a checked checkbox marks the task as incomplete
- [ ] Completed tasks show strikethrough styling on the title
- [ ] Completed tasks show dimmed opacity styling
- [ ] Visual feedback is instant upon click
- [ ] Completion state is saved to LocalStorage

**Priority:** P0 | **Feature Ref:** F1

---

### US-1.2: Visual Distinction for Completed Tasks

**As a** Robert Thompson (Retiree), **I want to** clearly see which tasks are done and which are not, **so that** I can quickly scan my daily list without confusion.

**Acceptance Criteria:**

- [ ] Completed tasks display with strikethrough on the title text
- [ ] Completed tasks display with reduced opacity (dimmed appearance)
- [ ] Active (incomplete) tasks display with normal styling
- [ ] Visual distinction updates immediately upon toggle
- [ ] Both strikethrough and dimming persist across page refresh

**Priority:** P0 | **Feature Ref:** F1

---

## 5. Epic: Task Editing (F2)

### Epic Description

Users can modify the title of existing tasks inline without opening dialogs or modal windows. Users enter edit mode via double-click or edit button, modify the text, and save or cancel their changes.

---

### US-2.1: Edit Task via Double-Click

**As a** Emma Rodriguez (University Student), **I want to** double-click a task to edit its title, **so that** I can update assignment details when professors change requirements.

**Acceptance Criteria:**

- [ ] Double-clicking a task title enters edit mode
- [ ] Task title is replaced with an input field in edit mode
- [ ] Previous title text is pre-populated in the input
- [ ] Pressing Enter saves the changes and exits edit mode
- [ ] Pressing Escape cancels the changes and exits edit mode
- [ ] Clicking outside the input (blur) saves the changes

**Priority:** P0 | **Feature Ref:** F2

---

### US-2.2: Cancel Edit on Escape Key

**As a** Marcus Chen (Busy Professional), **I want to** press Escape to cancel my edit, **so that** I can discard changes quickly without waiting for a save confirmation.

**Acceptance Criteria:**

- [ ] Pressing Escape while in edit mode cancels all changes
- [ ] Task reverts to its original title after cancellation
- [ ] UI exits edit mode and returns to view mode
- [ ] No changes are saved to LocalStorage

**Priority:** P0 | **Feature Ref:** F2

---

### US-2.3: Reject Empty Titles on Edit

**As a** Robert Thompson (Retiree), **I want to** have my task revert to the original title if I try to save an empty title, **so that** I don't accidentally delete task names when making edits.

**Acceptance Criteria:**

- [ ] Saving an empty title reverts to the original title
- [ ] Saving a whitespace-only title reverts to the original title
- [ ] No error message appears (silent revert for simplicity)
- [ ] Original title is preserved in the task list

**Priority:** P0 | **Feature Ref:** F2

---

## 6. Epic: Task Deletion (F3)

### Epic Description

Users can remove tasks from the list entirely. Each task displays a delete button that immediately removes the task upon click. No confirmation dialog is shown to maintain speed and simplicity.

---

### US-3.1: Delete Task via Button

**As a** Robert Thompson (Retiree), **I want to** click a delete button to remove a task, **so that** I can quickly clean up tasks that are no longer relevant.

**Acceptance Criteria:**

- [ ] Each task displays a delete button or icon
- [ ] Clicking the delete button removes the task immediately
- [ ] Task is removed from the task list
- [ ] Task element is removed from the display
- [ ] No confirmation dialog appears
- [ ] Deleted task is not recoverable

**Priority:** P0 | **Feature Ref:** F3

---

### US-3.2: Immediate Deletion Without Confirmation

**As a** Marcus Chen (Busy Professional), **I want to** delete tasks instantly without confirmation dialogs, **so that** I can quickly clear irrelevant tasks without friction.

**Acceptance Criteria:**

- [ ] Delete action completes in a single click
- [ ] No dialog or popup appears before deletion
- [ ] Task is removed immediately upon click
- [ ] Performance feels instant (< 100ms perceived latency)

**Priority:** P0 | **Feature Ref:** F3

---

## 7. Epic: Data Persistence (F4)

### Epic Description

All tasks are stored in browser LocalStorage, ensuring data survives page refreshes and browser sessions. Every change to tasks triggers an auto-save. The application loads saved tasks on startup.

---

### US-4.1: Auto-Save on Task Changes

**As a** Emma Rodriguez (University Student), **I want to** have my tasks automatically saved, **so that** I never lose work when I close my browser or the page refreshes.

**Acceptance Criteria:**

- [ ] Creating a new task saves to LocalStorage immediately
- [ ] Editing a task saves to LocalStorage immediately
- [ ] Deleting a task saves to LocalStorage immediately
- [ ] Toggling completion saves to LocalStorage immediately
- [ ] LocalStorage updates complete within 50ms

**Priority:** P0 | **Feature Ref:** F4

---

### US-4.2: Load Tasks on Page Load

**As a** Robert Thompson (Retiree), **I want to** see my tasks when I return to the app, **so that** I can continue from where I left off.

**Acceptance Criteria:**

- [ ] Saved tasks load when the page initially loads
- [ ] All task data (title, completion status) is restored correctly
- [ ] Tasks appear in the same order as when saved
- [ ] Page load completes within 1 second
- [ ] Empty state displays when no tasks exist

**Priority:** P0 | **Feature Ref:** F4

---

### US-4.3: Handle Corrupted Data Gracefully

**As a** Marcus Chen (Busy Professional), **I want to** see an empty task list if my data becomes corrupted, **so that** the app remains usable without technical errors.

**Acceptance Criteria:**

- [ ] Corrupted LocalStorage data is detected
- [ ] Corrupted data resets to an empty task list
- [ ] No error message appears to the user
- [ ] App remains fully functional after reset

**Priority:** P0 | **Feature Ref:** F4

---

### US-4.4: Handle Storage Unavailable

**As a** Emma Rodriguez (University Student), **I want to** be notified if storage is unavailable, **so that** I understand why my tasks cannot be saved.

**Acceptance Criteria:**

- [ ] App detects when LocalStorage is unavailable
- [ ] Warning message displays indicating storage is unavailable
- [ ] App continues to function in read-only mode
- [ ] User understands changes will not persist

**Priority:** P0 | **Feature Ref:** F4

---

## 8. Epic: Clear Completed Tasks (F5)

### Epic Description

Users can bulk-remove all completed tasks from the list in a single action. The button is only visible when there is at least one completed task, keeping the interface clean.

---

### US-5.1: Clear All Completed Tasks

**As a** Emma Rodriguez (University Student), **I want to** remove all completed tasks at once, **so that** I can start each week with a fresh list after finishing my assignments.

**Acceptance Criteria:**

- [ ] "Clear completed" button is visible when completed tasks exist
- [ ] Clicking the button removes all completed tasks
- [ ] Active (incomplete) tasks remain in the list
- [ ] Button disappears when no completed tasks remain
- [ ] Storage is updated after clearing

**Priority:** P2 | **Feature Ref:** F5

---

### US-5.2: Hide Button When No Completed Tasks

**As a** Marcus Chen (Busy Professional), **I want to** not see the Clear button when there are no completed tasks, **so that** my interface remains clean and uncluttered.

**Acceptance Criteria:**

- [ ] Button only displays when at least one task is completed
- [ ] Button hidden by default when all tasks are active
- [ ] Button appears dynamically as tasks are completed
- [ ] Button disappears immediately when last completed task is deleted

**Priority:** P2 | **Feature Ref:** F5

---

## 9. Epic: Task Counter (F6)

### Epic Description

Display counts of active vs. completed tasks to provide a quick status overview. The counter updates dynamically as tasks are completed or added.

---

### US-6.1: Display Active Task Count

**As a** Robert Thompson (Retiree), **I want to** see how many tasks remain, **so that** I can quickly gauge my daily workload.

**Acceptance Criteria:**

- [ ] Counter displays count of incomplete (active) tasks
- [ ] Counter text reads "X items left" format
- [ ] Counter updates dynamically when tasks are added
- [ ] Counter updates dynamically when tasks are completed
- [ ] Counter updates dynamically when tasks are deleted
- [ ] Counter positioned in visible footer or header area

**Priority:** P3 | **Feature Ref:** F6

---

### US-6.2: Singular/Plural Handling

**As a** Emma Rodriguez (University Student), **I want to** see grammatically correct counter text, **so that** my interface looks polished and professional.

**Acceptance Criteria:**

- [ ] Single item displays as "1 item left"
- [ ] Multiple items displays as "X items left"
- [ ] Singular/plural switches correctly when count changes
- [ ] Grammar updates immediately upon count change

**Priority:** P3 | **Feature Ref:** F6

---

## 10. Story Index

| Story ID | Title | Priority | Feature Ref |
|----------|-------|----------|-------------|
| US-0.1 | Add New Task via Enter Key | P0 | F0 |
| US-0.2 | Add New Task via Button Click | P0 | F0 |
| US-0.3 | Prevent Empty Task Submission | P0 | F0 |
| US-0.4 | Task Title Character Limit | P0 | F0 |
| US-1.1 | Toggle Task Completion via Checkbox | P0 | F1 |
| US-1.2 | Visual Distinction for Completed Tasks | P0 | F1 |
| US-2.1 | Edit Task via Double-Click | P0 | F2 |
| US-2.2 | Cancel Edit on Escape Key | P0 | F2 |
| US-2.3 | Reject Empty Titles on Edit | P0 | F2 |
| US-3.1 | Delete Task via Button | P0 | F3 |
| US-3.2 | Immediate Deletion Without Confirmation | P0 | F3 |
| US-4.1 | Auto-Save on Task Changes | P0 | F4 |
| US-4.2 | Load Tasks on Page Load | P0 | F4 |
| US-4.3 | Handle Corrupted Data Gracefully | P0 | F4 |
| US-4.4 | Handle Storage Unavailable | P0 | F4 |
| US-5.1 | Clear All Completed Tasks | P2 | F5 |
| US-5.2 | Hide Button When No Completed Tasks | P2 | F5 |
| US-6.1 | Display Active Task Count | P3 | F6 |
| US-6.2 | Singular/Plural Handling | P3 | F6 |

---

## 11. Summary

| Category | Count |
|----------|-------|
| Total User Stories | 19 |
| P0 (Critical) | 15 |
| P1 (High) | 0 |
| P2 (Medium) | 2 |
| P3 (Low) | 2 |
| Features Covered | F0, F1, F2, F3, F4, F5, F6 |
| Personas Referenced | Marcus Chen, Emma Rodriguez, Robert Thompson |

---

*End of User Stories — TodoApp*