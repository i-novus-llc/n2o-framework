import Alert from './Alerts/Alert'
import { Collapse } from './Collapse/Collapse'
import { Icon } from './Icon/Icon'
import PanelContainer from './Panel/PanelShortHand'
import { Title } from './Typography/Title/Title'
import { Text } from './Typography/Text/Text'
import Paragraph from './Typography/Paragraph/Paragraph'
import { Spinner } from './Spinner/Spinner'
import { PopoverConfirm } from './PopoverConfirm/PopoverConfirm'
import SearchBarContainer from './SearchBar/SearchBarContainer'
import { Drawer } from './Drawer/Drawer'
import { StatusText } from './StatusText/StatusText'
import { Image } from './Image/Image'
import { Status } from './Status/Status'
import { renderBadge } from './Badge/Badge'

export default {
    Alert,
    Collapse,
    Icon,
    Panel: PanelContainer,
    Text,
    Title,
    Paragraph,
    Spinner,
    PopoverConfirm,
    SearchBar: SearchBarContainer,
    Drawer,
    StatusText,
    Image,
    Status,
    Badge: renderBadge,
}
