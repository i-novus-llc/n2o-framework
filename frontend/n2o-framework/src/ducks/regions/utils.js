import find from 'lodash/find'
import get from 'lodash/get'

export function getTabsRegions(state) {
    const { regions = {} } = state

    return Object.values(regions).filter(region => region.tabs) || []
}

function checkFieldsByError(rows, fieldsWithErrors) {
    return rows.some(({ cols }) => cols
        .some(({ fields }) => fields
            .some(({ id }) => fieldsWithErrors.includes(id))))
}

export function checkTabErrors(content = [], fieldsWithErrors) {
    return content.some(({ form = {}, content }) => {
        if (content) {
            return content.some(({ tabs }) => checkTabsErrors(tabs, fieldsWithErrors))
        }

        const { fieldsets = [] } = form

        return fieldsets.some(({ rows }) => checkFieldsByError(rows, fieldsWithErrors))
    })
}

function checkTabsErrors(tabs, fieldsWithErrors) {
    return tabs.some(({ content }) => checkTabErrors(content, fieldsWithErrors))
}

export function activeTabHasErrors(activeEntity, tabs) {
    if (!activeEntity) {
        return false
    }

    const activeTabMeta = find(tabs, ({ id }) => activeEntity === id)

    return get(activeTabMeta, 'invalid', false)
}
