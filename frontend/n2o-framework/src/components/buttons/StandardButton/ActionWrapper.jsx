import React, { Fragment } from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'

import { withLinkAction } from './withLinkAction'
import { withPerformAction } from './withPerformAction'

/**
 * Обёртки для ХОКа withLinkAction и PerformActionWrapper
 * Необходим т.к. текущая реализация логики открытия завязана на последовательность событий:
 * 1) callAction из withCell, который кладёт данные модели в стор
 * 2) onClick самого withLinkAction, который берёт из стора и формирует по ним ссылку для перехода
 * и разделения параметров, необходимых для отображения картинки и открытия записи
 * @property {object} props
 * @property {string} props.url Шаблон адреса открываемой записи
 * @property {object} props.pathMapping Объект подстановки данных в адрес
 * @property {object} props.queryMapping Объект параметров запроса в адрес
 * @property {object} action
 * @property {'application' | '_blank'} [props.target] Тип открытия записи
 * @property children
 */

export const LinkActionWrapper = withLinkAction(props => React.createElement('div', props))
export const PerformActionWrapper = withPerformAction(props => React.createElement('div', props))

export function ActionWrapper({ url, target, pathMapping, queryMapping, action, className, children }) {
    let Wrapper = Fragment
    let wrapperProps = {}

    if (url || target) {
        Wrapper = LinkActionWrapper
        wrapperProps = {
            url,
            pathMapping,
            queryMapping,
            target,
            className,
        }
    }

    if (!isEmpty(action)) {
        Wrapper = PerformActionWrapper
        wrapperProps = {
            action,
            className,
        }
    }

    return <Wrapper {...wrapperProps}>{children}</Wrapper>
}

ActionWrapper.propTypes = {
    url: PropTypes.string,
    target: PropTypes.string,
    queryMapping: PropTypes.object,
    pathMapping: PropTypes.object,
    action: PropTypes.object,
    className: PropTypes.string,
    children: PropTypes.oneOfType([PropTypes.node, PropTypes.func]),
}
