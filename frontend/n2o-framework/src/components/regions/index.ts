// Temporary factory imports
import Navigation from '../navigation/index'
import Buttons from '../buttons/index-new-api'
import Layout from '../layout/index'
// Temporary factory imports

import { NoneRegion } from './None/NoneRegion'
import { TabsRegion } from './Tabs/Region'
import ListRegion from './List/ListRegion'
import { PanelRegion } from './Panel/PanelRegion'
import ScrollSpyRegion from './ScrollSpy/Region'
import { SubPage } from './SubPage/Region'

export default {
    NoneRegion,
    TabsRegion,
    ListRegion,
    PanelRegion,
    ScrollSpyRegion,
    SubPage,
    ...Navigation,
    ...Buttons,
    ...Layout,
}
