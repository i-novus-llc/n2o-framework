import React, { Fragment, ReactNode } from 'react'
import isEmpty from 'lodash/isEmpty'

import { type ButtonLinkProps, type ActionButtonProps } from '../withActionButton'

import { withPerformAction } from './withPerformAction'

/**
 * Обёртка для ХОКа PerformActionWrapper
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

export type Props = ButtonLinkProps & Pick<ActionButtonProps, 'action' | 'className'> & { children: ReactNode }

export const PerformActionWrapper = withPerformAction((props: Props) => React.createElement('div', props))

export function ActionWrapper({ url, target, pathMapping, queryMapping, action, className, children }: Props) {
    let Wrapper = Fragment
    let wrapperProps = {}

    if (!isEmpty(action || url)) {
        Wrapper = PerformActionWrapper
        wrapperProps = { url, target, queryMapping, pathMapping, action, className }
    }

    return <Wrapper {...wrapperProps}>{children}</Wrapper>
}
