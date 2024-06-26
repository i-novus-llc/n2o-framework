import React, { Component } from 'react'
import classNames from 'classnames'
import isEqual from 'lodash/isEqual'
import invoke from 'lodash/invoke'
import omit from 'lodash/omit'

import { Position, Shape } from '../../display/Badge/enums'

import { PopupItems } from './PopupItems'
import { isBottom } from './utils'
import { TOption, PopUpProps } from './types'

/**
 * Компонент попапа для {@link InputSelect}
 * @reactProps {array} options - массив данных
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данны
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {object} badge - данные для баджа
 * @reactProps {string} enabledFieldId - поле для активности
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} format - формат
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {array} selected - выбранные элементы
 * @reactProps {function} onSelect - callback при выборе элемента
 * @reactProps {function} onRemoveItem - callback при удаление элемента
 * @reactProps {boolean} needAddFilter
 * @reactProps {node} children - элемент потомок компонента PopupList
 * @reactProps {function} handleMouseEnter - обработчик для события onMouseEnter
 * @reactProps {function} handleMouseLeave - обработчик для события onMouseLeave
 */

export interface BadgeType {
    colorFieldId: string
    fieldId: string
    imageFieldId: string
    imagePosition: Position
    imageShape: Shape
    position: Position
    shape: Shape
}

interface Props {
    activeValueId?: string | number | null
    autocomplete?: boolean
    badge?: BadgeType
    children?: React.ReactNode
    count?: number
    descriptionFieldId?: string
    enabledFieldId?: string
    disabledValues?: Array<string | number>
    fetchData(arg: object, concat?: boolean, cacheReset?: boolean): void
    filterValue?: Record<string, unknown>
    format?: string
    groupFieldId: string
    hasCheckboxes: boolean
    iconFieldId: string
    imageFieldId: string
    isExpanded?: boolean
    labelFieldId: string
    loading?: boolean
    needAddFilter?: boolean
    onRemoveItem?(item: TOption): void
    onSelect(item: TOption): void
    options?: TOption[]
    page?: number
    popUpItemRef?: PopUpProps['popUpItemRef']
    renderIfEmpty?: boolean
    scheduleUpdate?(): void
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    selected?: any[]
    setActiveValueId?(id: string | null): void
    setMenuElement?(): void
    size?: number
    statusFieldId?: string
    style?: object
    valueFieldId: string
    multiSelect?: boolean
    searchMinLengthHint?: string | null | JSX.Element
}

interface State { menuElement: HTMLDivElement | null }

export class PopupList extends Component<Props> {
    state: State = { menuElement: null }

    onScroll = (e: Event) => {
        const { needAddFilter, filterValue, fetchData, page, size, count, loading } = this.props

        if (isBottom(e.target as Element) && !loading && (page && size && count) && (page * size < count)) {
            const filter = needAddFilter ? filterValue : {}

            fetchData({ page: page + 1, ...filter }, true)
        }
    }

    componentDidUpdate(prevProps: Props, prevState: State) {
        const { options } = this.props
        const { menuElement } = this.state

        if (!isEqual(prevProps.options, options)) {
            invoke(this.props, 'scheduleUpdate')
        }

        if (!prevState.menuElement && menuElement) {
            menuElement.addEventListener('scroll', this.onScroll)
        }
    }

    componentWillUnmount() {
        const { menuElement } = this.state

        if (menuElement) { menuElement.removeEventListener('scroll', this.onScroll) }
    }

    setMenuElement = (menuElement: State['menuElement']) => { this.setState({ menuElement }) }

    render() {
        const { children, style, ...rest } = this.props

        return (
            <div className="n2o-pop-up__wrapper">
                <div
                    style={style}
                    className={classNames('n2o-dropdown-control n2o-pop-up')}
                    ref={this.setMenuElement}
                >
                    {children}
                    <PopupItems {...omit(rest, ['needAddFilter', 'filterValue', 'expandPopUp', 'fetchData', 'isExpanded'])} />
                </div>
            </div>
        )
    }
}
