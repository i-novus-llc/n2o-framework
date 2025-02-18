import { Drawer } from '@i-novus/n2o-components/lib/display/Drawer'
import { Html } from '@i-novus/n2o-components/lib/display/Html'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'
import { Image } from '@i-novus/n2o-components/lib/display/Image/Image'
import { Pagination } from '@i-novus/n2o-components/lib/display/Pagination/Pagination'
import { Spinner } from '@i-novus/n2o-components/lib/layouts/Spinner/Spinner'
import { Status } from '@i-novus/n2o-components/lib/display/Status'
import { StatusText } from '@i-novus/n2o-components/lib/display/StatusText/StatusText'
import { Tabs } from '@i-novus/n2o-components/lib/display/Tabs/Tabs'
import { Tooltip } from '@i-novus/n2o-components/lib/display/Tooltip'

import Alert from './Alerts/Alert'
import { Collapse } from './Collapse/Collapse'
import { PanelContainer as Panel } from './Panel/PanelContainer'
import SearchBarContainer from './SearchBar/SearchBarContainer'
import { Badge } from './Badge/Badge'

export default {
    Alert,
    Collapse,
    Icon,
    Panel,
    Spinner,
    SearchBar: SearchBarContainer,
    Drawer,
    StatusText,
    Image,
    Status,
    Badge,
    Html,
    Tooltip,
    Pagination,
    Tabs,
}
