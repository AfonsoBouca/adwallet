# Sprint 2 Defect Log

## Summary
Total bugs found: 3
Total bugs fixed: 3
Escaped to next sprint: 0

## Defects

| Bug ID | Description | Root Cause | Resolution | PR | Status |
|--------|-------------|------------|-----------|----|---------
| D001 | Missing UI screens breaking CI builds | Feature branches missed some core UI dependencies from local prototype | Restored missing UI screens (AdFeed, Wallet, etc.) in a fix branch | #74 | Fixed |
| D002 | Insufficient balance error not shown | ViewModel state was not properly triggering Snackbar | Added error state flow to ViewModel | #70 | Fixed |
| D003 | Notification toggle not persisting | SharedPreferences.edit() was missing .apply() | Added .apply() to persistence logic | #73 | Fixed |

## Example Defects
- **Missing UI screens breaking CI builds**: Fixed by creating a dedicated `fix/missing-core-files` branch to restore missing dependencies and unblock the CI pipeline.
- **Insufficient balance error not shown**: Fixed by adding a dedicated error event flow to `DonationConfirmationViewModel`.
- **Notification toggle not persisting**: Fixed by ensuring `SharedPreferences.edit().apply()` is called whenever a toggle state changes.

---

Logged by: André
Last updated: May 15, 2026
