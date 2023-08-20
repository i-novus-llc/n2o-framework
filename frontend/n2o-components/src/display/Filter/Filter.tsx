import React, { KeyboardEvent, ReactNode, useCallback } from 'react'
import classNames from 'classnames'

import { EventHandlersContext } from '../../inputs/eventHandlersContext'

// TODO выпилить в следующих версиях, Buttons тут абсолютно не нужен, т.к. есть более функциональный FilterButtonsField
import { Buttons, ButtonsProps } from './Buttons'

interface FilterProps extends ButtonsProps {
    children: ReactNode,
    hideButtons?: boolean
}

export function Filter({
    className,
    style,
    visible,
    hideButtons,
    searchLabel,
    resetLabel,
    children,
    onSearch,
    onReset,
}: FilterProps) {
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

            <Buttons
                visible={!hideButtons}
                searchLabel={searchLabel}
                resetLabel={resetLabel}
                onSearch={onSearch}
                onReset={onReset}
            />
        </div>
    ) : null
}
