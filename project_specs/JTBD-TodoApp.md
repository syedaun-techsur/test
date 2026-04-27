# Jobs-to-be-Done Specification — TodoApp

**Document Version:** 1.0  
**Project:** TodoApp  
**Generated:** 2026-04-27  
**Related Personas:** PER-01, PER-02, PER-03  
**Related PRD:** PRD-TodoApp.md  

---

## 1. Document Overview

This document transforms persona goals and PRD features into outcome-driven job statements. Each job uses the "When/I want/So I can" format to capture the user's motivation and expected outcome rather than feature requirements. These JTBDs inform acceptance criteria, journey design, and verification downstream.

---

## 2. JTBD Summary Table

| JTBD-ID | Persona | Job Statement | Priority |
|---------|---------|---------------|----------|
| JTBD-01.1 | PER-01 (Marcus) | When I have a brief window between meetings, I want to capture a task in under 5 seconds, so I don't lose my thought | P0 |
| JTBD-01.2 | PER-01 (Marcus) | When I'm in a meeting and can't look at my screen, I want to mark tasks complete with a single click or keystroke, so I can track progress without breaking focus | P0 |
| JTBD-01.3 | PER-01 (Marcus) | When task details change, I want to edit the task inline immediately, so I can update context without opening menus | P1 |
| JTBD-01.4 | PER-01 (Marcus) | When I close my browser at the end of the day, I want tasks to persist automatically, so I don't lose any work | P0 |
| JTBD-02.1 | PER-02 (Emma) | When I think of a task between classes, I want to add it instantly without logging in, so I capture the thought before it escapes | P0 |
| JTBD-02.2 | PER-02 (Emma) | When I'm reviewing coursework, I want to clearly see which items are completed, so I can gauge my progress at a glance | P0 |
| JTBD-02.3 | PER-02 (Emma) | When the week ends, I want to clear all completed tasks at once, so I can start fresh for the new week | P2 |
| JTBD-02.4 | PER-02 (Emma) | When I open the app, I want to see exactly how many tasks remain, so I can plan my study time effectively | P1 |
| JTBD-03.1 | PER-03 (Robert) | When I think of a task, I want to add it without navigating any menus, so I can use the app without help | P0 |
| JTBD-03.2 | PER-03 (Robert) | When I complete a task, I want to clearly see it's marked done, so I get the satisfaction of crossing it off | P0 |
| JTBD-03.3 | PER-03 (Robert) | When I return to my computer later, I want tasks to still be there, so I don't lose track of what's pending | P0 |
| JTBD-03.4 | PER-03 (Robert) | When my grandchildren use my tablet, I want my tasks to remain private, so they don't accidentally see or delete my items | P1 |

---

## 3. Per-Persona Job Sections

### PER-01: Marcus Chen — Busy Professional

**Context:** Marcus works in an open office with constant interruptions. He has multiple brief windows (5-15 minutes) between meetings where he needs to quickly capture tasks without losing his train of thought. He uses his browser throughout the workday and cannot install software or create accounts due to IT restrictions.

---

#### JTBD-01.1: Quick Task Capture

**Job Statement:**  
When I have a brief window between meetings (5-15 minutes), I want to capture a task in under 5 seconds using only keyboard input, so I don't lose my thought before my next meeting starts.

**Current Alternatives:**
- Opens 3 separate spreadsheets and cross-references manually
- Sends himself email reminders that get buried in inbox
- Relies on memory which fails when multiple tasks accumulate

**Hiring Criteria:**
- Task appears in list within 1 second of pressing Enter
- Input field clears automatically after submission
- No mouse interaction required for task entry
- Works without any account creation

**Success Measure:** Marcus can add 3 new tasks in under 12 seconds total, starting from empty list to all three visible.

**Related Features:** F0 (Task Creation), F4 (Data Persistence)  
**Priority:** P0

---

#### JTBD-01.2: Meeting-Roam Task Completion

**Job Statement:**  
When I'm in a meeting and can't fully look at my screen, I want to mark tasks complete with a single click or keystroke, so I can track progress without breaking eye contact with meeting participants.

**Current Alternatives:**
- Waits until meeting ends to update task list, forgetting half the items
- Uses paper notepad that gets lost
- Tells colleague verbally and forgets

**Hiring Criteria:**
- Checkbox toggles on single click (mouse) or spacebar (keyboard)
- Visual feedback is distinct and viewable from 3 feet away
- Completed tasks show strikethrough or color change
- State persists immediately without page reload

**Success Measure:** Marcus can toggle 5 task completion states in under 10 seconds without looking at the screen.

**Related Features:** F1 (Task Completion Toggle)  
**Priority:** P0

---

#### JTBD-01.3: Inline Task Editing

**Job Statement:**  
When task details change mid-day (new requirements from client, additional context from meeting), I want to edit the task title inline immediately, so I can update context without opening menus or switching screens.

**Current Alternatives:**
- Deletes old task and creates new one with updated details
- Adds new task with "UPDATE:" prefix, cluttering list
- Uses separate note app for updated details

**Hiring Criteria:**
- Double-click or single button press enters edit mode
- Edit mode shows text input pre-filled with current title
- Enter key saves, Escape key cancels
- No dialog or modal opens

**Success Measure:** Marcus can edit a task title and save changes in under 5 seconds.

**Related Features:** F2 (Task Editing)  
**Priority:** P1

---

#### JTBD-01.4: Automatic Data Persistence

**Job Statement:**  
When I close my browser at the end of the day or experience a connection drop, I want tasks to persist automatically without manual saving, so I don't lose any work I've captured.

**Current Alternatives:**
- Manually exports to spreadsheet at end of each day
- Sends email copy to himself as backup
- Accepts task loss and re-enters tasks next morning

**Hiring Criteria:**
- Auto-save triggers within 500ms of any change
- All tasks load automatically on page reopen
- No "save" button or confirmation required
- Handles browser crash gracefully (data intact on reopen)

**Success Measure:** After adding 10 tasks and closing browser without any manual action, all 10 tasks are present when reopening the app.

**Related Features:** F4 (Data Persistence)  
**Priority:** P0

---

### PER-02: Emma Rodriguez — University Student

**Context:** Emma is a 21-year-old university student managing 25-30 tasks at any time, mixing coursework, lab work, part-time job shifts, and personal errands. She switches between contexts frequently throughout her day and cannot afford to spend mental energy on tool management. She uses both laptop and mobile.

---

#### JTBD-02.1: Instant Task Capture Without Account

**Job Statement:**  
When I think of a task between classes or while walking to the library, I want to add it instantly without logging in or setting up an account, so I capture the thought before it escapes and get back to what I was doing.

**Current Alternatives:**
- Sends herself text message that gets lost in SMS list
- Uses Notes app but loses phone password
- Tries to remember and forgets

**Hiring Criteria:**
- App loads in under 2 seconds on mobile connection
- No login, signup, or password required
- Single text input + enter adds task
- Works offline

**Success Measure:** Emma can add 2 new tasks within 30 seconds of opening the app for the first time on a new device.

**Related Features:** F0 (Task Creation), F4 (Data Persistence)  
**Priority:** P0

---

#### JTBD-02.2: Visual Progress Tracking

**Job Statement:**  
When I'm reviewing my coursework list at the end of the day, I want to clearly see which items are completed versus pending, so I can visually gauge my progress and feel motivated by what's done.

**Current Alternatives:**
- Opens each task individually to check status
- Creates separate "done" categories in other apps
- Uses paper checklist that gets filled and discarded

**Hiring Criteria:**
- Completed tasks show clear visual distinction (strikethrough, color, opacity)
- Completed items remain visible in list (not hidden or archived)
- Toggle provides instant visual feedback
- Both phone and desktop show consistent visual states

**Success Measure:** Emma can accurately categorize 10 mixed complete/incomplete tasks by visual inspection alone in under 5 seconds.

**Related Features:** F1 (Task Completion Toggle)  
**Priority:** P0

---

#### JTBD-02.3: Bulk Clear Completed Tasks

**Job Statement:**  
When the week ends and I've finished my coursework, I want to clear all completed tasks at once with a single action, so I can start fresh for the new week without deleting each item individually.

**Current Alternatives:**
- Deletes each completed task one by one (tedious)
- Creates new list in new app, abandoning old one
- Keeps old completed tasks cluttering list indefinitely

**Hiring Criteria:**
- "Clear completed" button appears only when completed tasks exist
- All completed tasks removed in single action
- Active/pending tasks remain untouched
- No confirmation dialog (keep it fast)

**Success Measure:** Emma can clear 8 completed tasks with a single tap/click in under 3 seconds.

**Related Features:** F5 (Clear Completed Tasks)  
**Priority:** P2

---

#### JTBD-02.4: Remaining Task Count

**Job Statement:**  
When I open the app to plan my study session, I want to see exactly how many tasks remain incomplete, so I can realistically estimate how much time I need to allocate.

**Current Alternatives:**
- Counts incomplete items manually (error-prone)
- Creates separate "to do" list in different app
- Guesses and either underestimates or overestimates time

**Hiring Criteria:**
- Counter displays prominently in header or footer
- Updates dynamically within 500ms of any change
- Shows clear "X items left" format
- Works on both mobile and desktop

**Success Measure:** Emma can accurately assess remaining workload within 2 seconds of opening the app.

**Related Features:** F6 (Task Counter)  
**Priority:** P1

---

### PER-03: Robert Thompson — Retiree

**Context:** Robert is a 68-year-old retired teacher who uses both desktop computer at home and tablet when traveling. He is comfortable with email and basic web browsing but is wary of anything that seems complex or asks for personal information. He sometimes shares his tablet with grandchildren.

---

#### JTBD-03.1: Menu-Free Task Addition

**Job Statement:**  
When I think of a task while watching TV, I want to add it without navigating any menus or reading instructions, so I can use the app without asking for help each time.

**Current Alternatives:**
- Writes tasks on paper list that gets lost
- Asks children to help add tasks digitally
- Doesn't capture tasks because too complicated

**Hiring Criteria:**
- Single input field visible on page load
- Pressing Enter after typing adds task
- No icons, menus, or options to navigate
- Works the same way every time

**Success Measure:** Robert can add 2 new tasks within 60 seconds of opening the app for the first time, without assistance.

**Related Features:** F0 (Task Creation)  
**Priority:** P0

---

#### JTBD-03.2: Clear Completion Visibility

**Job Statement:**  
When I complete a task like taking medication or finishing an errand, I want to clearly see it's marked as done, so I get the satisfaction of crossing it off and know I don't need to do it again.

**Current Alternatives:**
- Physically crosses off paper list
- Moves task to separate "done" section in complex apps
- Deletes completed tasks to "clear" the list

**Hiring Criteria:**
- Completed task shows strikethrough text or clear color change
- Checkbox shows clear checked/unchecked state
- Completed tasks remain visible (not disappear)
- Visual feedback is obvious at glance

**Success Measure:** Robert can identify which of 5 tasks are completed without clicking or interacting, just by looking at the screen.

**Related Features:** F1 (Task Completion Toggle)  
**Priority:** P0

---

#### JTBD-03.3: Reliable Persistence Across Days

**Job Statement:**  
When I close my browser or turn off my computer and return the next day, I want my tasks to still be there, so I don't lose track of what's pending and don't need to re-enter everything.

**Current Alternatives:**
- Writes tasks on paper that gets lost or misplaced
- Takes photo of screen to "persist" tasks
- Expects loss and doesn't rely on digital tools

**Hiring Criteria:**
- All tasks persist automatically without any action
- Works across days, weeks, browser restarts
- No "account" or "cloud" required
- Data survives computer restart

**Success Measure:** Robert can close the browser, wait 24 hours, reopen the app, and find all 8 previously-added tasks intact.

**Related Features:** F4 (Data Persistence)  
**Priority:** P0

---

#### JTBD-03.4: Task Privacy from Shared Device

**Job Statement:**  
When my grandchildren use my tablet to play games, I want my task list to remain private and unchanged, so they don't accidentally see my medical appointments or delete my tasks thinking they are games.

**Current Alternatives:**
- Uses paper list instead of digital (privacy but less convenient)
- Clears tablet before grandkids arrive, losing own tasks
- Uses complex password-protected apps that are hard to use

**Hiring Criteria:**
- Tasks not visible without opening the app
- Accidental taps don't delete or modify tasks
- No pop-ups or notifications appear
- Simple enough for Robert to use without grandchild interference

**Success Measure:** After a grandchild uses the tablet for 30 minutes with unrestricted access, all 6 of Robert's tasks remain intact and unchanged.

**Related Features:** F3 (Task Deletion), F4 (Data Persistence)  
**Priority:** P1

---

## 4. Outcome-to-Feature Traceability

| JTBD-ID | Expected Outcome | Related Features | Feature IDs |
|--------|-----------------|----------------|------------|
| JTBD-01.1 | Add task in <5s without mouse | Task Creation, No Account Required | F0 |
| JTBD-01.2 | Toggle completion single-click/keyboard | Task Completion Toggle | F1 |
| JTBD-01.3 | Edit inline without opening menus | Task Editing | F2 |
| JTBD-01.4 | Tasks persist after browser close | Data Persistence | F4 |
| JTBD-02.1 | Add task without login | Task Creation, No Account Required | F0, F4 |
| JTBD-02.2 | Visual distinction complete/pending | Task Completion Toggle | F1 |
| JTBD-02.3 | Bulk clear completed in one action | Clear Completed Tasks | F5 |
| JTBD-02.4 | See remaining task count | Task Counter | F6 |
| JTBD-03.1 | Add task without menus | Task Creation | F0 |
| JTBD-03.2 | Clear done/not-done visibility | Task Completion Toggle | F1 |
| JTBD-03.3 | Tasks persist across days | Data Persistence | F4 |
| JTBD-03.4 | Tasks intact after shared use | Data Persistence | F4 |

---

## 5. NaC Preview (Candidate Natural Acceptance Criteria)

| JTBD-ID | Outcome | Candidate Acceptance Criteria |
|--------|---------|-------------------------------|
| JTBD-01.1 | Add task in <5s | Given empty task list, when user types task title and presses Enter, then task appears in list within 1 second |
| JTBD-01.2 | Toggle with single click | Given task exists, when user clicks checkbox or presses spacebar, then task shows completed state immediately |
| JTBD-01.3 | Edit inline | Given task exists, when user double-clicks task and changes title and presses Enter, then updated title persists |
| JTBD-01.4 | Persistence after close | Given tasks exist, when user closes browser and reopens, then all tasks remain |
| JTBD-02.1 | Add without login | Given fresh browser, when user opens app and adds task, then no login or signup required |
| JTBD-02.2 | Visual progress | Given mixed complete/incomplete tasks, when user views list, then completed show strikethrough |
| JTBD-02.3 | Bulk clear | Given 3 completed tasks exist, when user clicks "clear completed", then all 3 removed, incomplete remain |
| JTBD-02.4 | Task count visible | Given tasks exist, when user opens app, then counter shows "X items left" in header |
| JTBD-03.1 | Menu-free add | Given empty list, when user types and presses Enter, then task added, no menus navigated |
| JTBD-03.2 | Clear done visibility | Given completed task, when user views list, then completed shows strikethrough |
| JTBD-03.3 | Multi-day persistence | Given tasks exist, when user returns after 24 hours, then all tasks present |
| JTBD-03.4 | Private from sharing | Given tasks exist, when child uses tablet freely, then tasks unchanged |

---

## 6. Validation Checklist

- [x] Every persona (PER-01, PER-02, PER-03) has at least 3 jobs (target: 3-4 per persona)
- [x] All job statements use "When/I want/So I can" format
- [x] Hiring criteria are specific and testable for each job
- [x] Success measures are quantifiable for each job
- [x] Each PRD feature (F0-F6) mapped to at least one JTBD
- [x] All 12 JTBDs have unique IDs following PER.NN convention
- [x] NaC Preview included for all jobs
- [x] Outcome-to-Feature traceability complete

---

*End of Document*