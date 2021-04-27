/**
 * Created by emamoshin on 19.06.2017.
 */
import isObject from 'lodash/isObject'
import each from 'lodash/each'

const defaultTypeList = ['single', 'multi', 'filter', 'context', 'control']

/**
 * FORMAT { PREFIX -> WidgetLink : FieldId }
 * @deprecated Клиентский парсинг глобальных ссылок. Устарел.
 * @param state
 * @param links
 * @returns {*}
 */
export function resolveLink(state, links) {
    const { widgets } = state
    let result

    if (isObject(links)) {
        result = {}
        each(links, (val, key) => {
            if (val && isLinkedString(val)) {
                const parsedLink = parseLink(val)

                if (parsedLink) {
                    const model = getFromState(
                        widgets,
                        parsedLink.prefix,
                        parsedLink.widget,
                    )

                    if (model) {
                        if (parsedLink.field) {
                            result[key] = model[parsedLink.field]
                        } else {
                            result[key] = model
                        }
                    } else {
                        result[key] = null
                    }
                }
            }
        })
    }

    return { ...links, ...result }
}

function isLinkedString(str) {
    str = String(str)
    const res = str.match('^{([^}^{]*)}$')

    if (res && res[1]) {
        return res[1]
    }

    return false
}

function parseLink(link) {
    const regExp = link.match('{(.*)->([^:]*):?(.*)}')

    if (regExp) {
        return {
            prefix: regExp[1],
            widget: regExp[2],
            field: regExp[3],
        }
    }
    console.warn('Global link not parsed! (?)'.format(link))
}

// todo: add other global link types
function getFromState(state, prefix, widgetId) {
    return state[widgetId] && state[widgetId].resolveModel
}
