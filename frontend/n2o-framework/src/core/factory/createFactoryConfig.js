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
}

export default function createFactoryConfig(customConfig = {}) {
    return merge(factories, customConfig)
}
