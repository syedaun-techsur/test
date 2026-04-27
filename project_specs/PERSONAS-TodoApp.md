# Persona Specifications — TodoApp

**Document Version:** 1.0  
**Project:** TodoApp  
**Generated:** 2026-04-27  
**Related PRD:** PRD-TodoApp.md  

---

## 1. Document Overview

This document expands the PRD's target user descriptions into detailed persona profiles. Each persona represents a distinct user archetype with specific goals, pain points, and tasks derived from the product requirements. These profiles inform downstream design decisions, user story prioritization, and UX specifications.

---

## 2. Persona Summary

| Persona ID | Name | Role | Primary Goal |
|------------|------|------|--------------|
| PER-01 | Marcus Chen | Busy Professional | Capture tasks in seconds during work breaks without context-switching overhead |
| PER-02 | Emma Rodriguez | University Student | Organize coursework and personal tasks in one place without account complexity |
| PER-03 | Robert Thompson | Retiree | Use a simple, no-signup task list that works reliably without technical complexity |

---

## 3. Per-Persona Profiles

### PER-01: Marcus Chen

**Role & Context:**

Marcus is a 34-year-old marketing manager at a mid-size company. He works in an open office environment with constant interruptions. During his day, he has multiple brief windows between meetings (5-15 minutes) where he needs to quickly capture tasks that come to mind without losing his train of thought. He uses his browser throughout the workday and cannot install software or create accounts due to IT restrictions. He manages about 15-20 personal to-do items at any time, mostly small action items that arise during work hours.

**Goals:**

- Add a task in under 5 seconds using only keyboard (F0, F1)
- Mark tasks complete during meetings without opening any menus (F1)
- Edit a task title immediately when he remembers more details (F2)
- Delete tasks that are no longer relevant without any confirmation dialogs (F3)
- Trust that tasks persist automatically without manual saving (F4)

**Pain Points:**

- Existing tools have too many features that slow down quick task entry (Problem: Complexity overload)
- Cannot install apps or create accounts on work devices (Problem: Accessibility barriers)
- Previous browser-based tools lost data when he closed the tab (Problem: Lack of persistence)
- Multi-step task entry process causes him to forget what he was about to add (Problem: Slow task entry)

**Technical Expertise:** Intermediate — Comfortable with web applications, prefers keyboard-driven workflows, avoids command-line tools. Uses Chrome and expects instant responses.

**Top Tasks:**

1. Add new task via keyboard (Enter key) — multiple times daily, critical
2. Toggle task completion by clicking checkbox — throughout day, high
3. Edit task title inline when details change — several times weekly, medium
4. Delete completed tasks — weekly, medium
5. Review "items left" count to gauge workload — daily, low

**Success Criteria:**

- Can add a new task in under 3 seconds (exceeds PRD's 5-second target)
- Zero task loss after browser restart
- Can complete all core actions without mouse interaction

---

### PER-02: Emma Rodriguez

**Role & Context:**

Emma is a 21-year-old junior at a state university, majoring in biology. She lives in a shared apartment with two roommates and studies primarily on her laptop at her desk or at the campus library. She manages roughly 25-30 tasks at any time, mixing coursework assignments, lab work, part-time job shifts, and personal errands. She needs to switch between contexts frequently throughout her day and cannot afford to spend mental energy on tool management. She shares a single laptop with her roommate occasionally and doesn't want task data visible to others.

**Goals:**

- Add tasks instantly between classes without logging in or setting up an account (F0, F4)
- Visually distinguish completed coursework from pending items (F1)
- Edit task names when assignment details change (F2)
- Remove tasks that are no longer needed without any friction (F3)
- Clear all completed assignments at the end of each week to start fresh (F5)
- See exactly how many assignments remain due (F6)

**Pain Points:**

- Account-based task apps require password management she doesn't have time for (Problem: Accessibility barriers)
- Lost an entire week's homework list when her browser cache cleared (Problem: Lack of persistence)
- Interfaces with too many features make it hard to focus on what matters (Problem: Complexity overload)
- Slow interfaces during library WiFi drops make her lose tasks (Problem: Slow task entry)

**Technical Expertise:** Intermediate — Comfortable with web browsing, mobile apps, and basic productivity tools. Uses Safari on Mac and Chrome on mobile. Expects clean, minimalist interfaces.

**Top Tasks:**

1. Add assignment or task immediately after it's assigned — multiple times daily, critical
2. Check off completed assignments to see progress — several times daily, high
3. View remaining task count to prioritize study sessions — daily, medium
4. Bulk-clear completed tasks at week end — weekly, medium
5. Edit task details when professor updates requirements — as-needed, medium

**Success Criteria:**

- Can start using the app without any setup or account creation
- Tasks survive browser close and laptop shutdown
- Mobile-responsive for checking tasks between classes

---

### PER-03: Robert Thompson

**Role & Context:**

Robert is a 68-year-old retired teacher who splits his time between his home in Florida and visiting his adult children. He uses a desktop computer at home and a tablet when traveling. He is comfortable with email and basic web browsing but is wary of anything that seems complex or asks for personal information. He maintains a simple daily task list of about 10-15 items: appointments, errands, medication reminders, and small home projects. He sometimes shares his tablet with grandchildren and needs tasks to remain private.

**Goals:**

- Add a task without navigating any menus or reading instructions (F0)
- Clearly see which tasks are done and which are not (F1)
- Change a task name if he mistyped it (F2)
- Remove a task if he no longer needs it (F3)
- Have confidence that tasks will be there when he returns to the computer later (F4)
- Quickly see how many things he still needs to do (F6)

**Pain Points:**

- Doesn't want to create accounts or remember passwords for simple tools (Problem: Accessibility barriers)
- Has lost tasks before when he accidentally closed a browser window (Problem: Lack of persistence)
- Gets frustrated when websites have too many buttons and options (Problem: Complexity overload)
- Needs things to work the same way every time he uses them (Problem: Slow task entry)

**Technical Expertise:** Basic — Uses web browser for email, weather, and light research. Prefers click interactions over keyboard shortcuts. Needs clear visual feedback for all actions.

**Top Tasks:**

1. Add a new task by typing and pressing Enter — daily, critical
2. Click checkbox to mark tasks complete — several times daily, high
3. Check remaining task count — daily, medium
4. Delete tasks that are no longer relevant — as-needed, medium
5. Edit task text when he notices a typo — as-needed, low

**Success Criteria:**

- Can use the app without any explanation or help
- Tasks persist reliably across days and browser sessions
- Works on both desktop and tablet without confusion

---

## 4. Persona Relationships

| Relationship | Persona A | Persona B | Nature |
|--------------|-----------|-----------|--------|
| None | PER-01 (Marcus) | PER-02 (Emma) | Different use contexts — work vs. school |
| None | PER-01 (Marcus) | PER-03 (Robert) | Different tech comfort levels and usage frequency |
| None | PER-02 (Emma) | PER-03 (Robert) | Different generations but similar core need for simplicity |

All three personas are independent users of the same single-user application. There are no collaborative workflows or role hierarchies. Each persona uses the application in isolation, though they may share device access in some cases (Emma shares a laptop; Robert shares a tablet with grandchildren).

---

## 5. Feature-Persona Matrix

| Feature | PER-01 (Marcus) | PER-02 (Emma) | PER-03 (Robert) |
|---------|-----------------|---------------|-----------------|
| **F0: Task Creation** | Primary | Primary | Primary |
| **F1: Task Completion Toggle** | Primary | Primary | Primary |
| **F2: Task Editing** | Secondary | Primary | Primary |
| **F3: Task Deletion** | Secondary | Secondary | Primary |
| **F4: Data Persistence** | Primary | Primary | Primary |
| **F5: Clear Completed Tasks** | None | Secondary | None |
| **F6: Task Counter** | Secondary | Primary | Primary |

### Matrix Legend

- **Primary:** Core to persona's daily use; drives feature prioritization
- **Secondary:** Used occasionally but not defining the experience
- **None:** Not relevant to this persona's goals

### Feature-Persona Mapping Rationale

- **F0, F1, F4 (Core):** All three personas rely on these as foundational to their success criteria. Task creation and persistence are explicitly cited in every persona's goals.
- **F2 (Edit):** Critical for Emma (assignment details change) and Robert (typos), useful for Marcus (context updates).
- **F3 (Delete):** Most important for Robert, who wants simple removal. Marcus and Emma use it less frequently.
- **F5 (Clear Completed):** Emma uses this weekly to reset; Marcus and Robert prefer individual deletion.
- **F6 (Counter):** Important for Emma (assignment tracking) and Robert (daily overview); secondary for Marcus who works from memory.

---

## 6. Validation Checklist

- [x] All PRD target users have a persona (3 personas from 3 user types)
- [x] Each persona has all required subsections (Identity, Role & Context, Goals, Pain Points, Technical Expertise, Top Tasks, Success Criteria)
- [x] Goals trace back to PRD features (F0-F6 referenced in goals)
- [x] Pain points trace back to PRD Problem Statement (4 problem areas mapped)
- [x] Feature-Persona Matrix covers all PRD features (F0 through F6)
- [x] No persona is a clone of another (distinct needs: speed/brevity, organization/education, simplicity/trust)
- [x] 3 personas total (within 2-4 range)

---

*End of Document*