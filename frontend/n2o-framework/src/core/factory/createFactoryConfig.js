import merge from 'deepmerge'

import controls from '../../components/controls'
import widgets from '../../components/widgets'
import regions from '../../components/regions'
import pages from '../../components/pages'
import headers from '../../components/widgets/Table/headers'
import cells from '../../components/widgets/Table/cells'
import fieldsets from '../../components/widgets/Form/fieldsets'
import fields from '../../components/widgets/Form/fields'
import actions from '../../impl/actions'
import buttons from '../../components/buttons'
import snippets from '../../components/snippets'
import overlays from '../../components/core/overlays'
import * as templates from '../../components/core/templates/templates'
import * as breadcrumbs from '../../components/core/Breadcrumb/breadcrumb'
import { headerItems } from '../../plugins/Header/SimpleHeader/Menu/FactoryItems'
import { sidebarItems } from '../../plugins/SideBar/Menu/FactoryItems'

export const factories = {
    controls,
    widgets,
    regions,
    pages,
    headers,
    cells,
    fieldsets,
    fields,
    actions,
    snippets,
    buttons,
    templates,
    overlays,
    breadcrumbs,
    headerItems,
    sidebarItems,
}

export default function createFactoryConfig(customConfig = {}) {
    return merge(factories, customConfig)
}
