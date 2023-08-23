import React, { KeyboardEvent, useCallback } from 'react'
import classNames from 'classnames'

import { EventHandlersContext } from '../../controls/eventHandlersContext'

// TODO выпилить в следующих версиях, FilterButtons тут абсолютно не нужен, т.к. есть более функциональный FilterButtonsField

import FilterButtons, { ButtonsProps } from './Buttons'

export interface FilterProps extends ButtonsProps {
    hideButtons?: boolean
    style?: object
    children: React.ReactNode
}

export function Filter(props: FilterProps) {
    const {
        className,
        style,
        visible,
        hideButtons,
        searchLabel,
        resetLabel,
        children,
        onSearch,
        onReset,
    } = props

    const { Provider } = EventHandlersContext

    const onKeyDown = useCallback((evt: KeyboardEvent<HTMLInputElement>) => {
        if (onSearch && evt.key === 'Enter' && evt.ctrlKey) {
            evt.stopPropagation()

            onSearch(evt)
        }
    }, [onSearch])

    return visible ? (
        <div className={classNames('n2o-filter', className)} style={style}>
            <Provider value={{ onKeyDown }}>
                {children}
            </Provider>

            <FilterButtons
                visible={!hideButtons}
                searchLabel={searchLabel}
                resetLabel={resetLabel}
                onSearch={onSearch}
                onReset={onReset}
            />
        </div>
    ) : null
}
