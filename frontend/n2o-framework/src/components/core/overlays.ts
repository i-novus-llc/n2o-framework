import { ConfirmDialog } from '../overlays/ConfirmDialog'
import { ConfirmPopover } from '../overlays/ConfirmPopover'

import PageDialog from './PageDialog'
import ModalPage from './ModalPage'
import DrawerPage from './DrawerPage'

export default {
    Modal: ModalPage,
    Drawer: DrawerPage,
    Dialog: PageDialog,
    ConfirmDialog,
    ConfirmPopover,
}
