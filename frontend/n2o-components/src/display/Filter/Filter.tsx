import React, { ReactNode } from 'react'
import classNames from 'classnames'

// TODO выпилить в следующих версиях, Buttons тут абсолютно не нужен, т.к. есть более функциональный FilterButtonsField

import { Buttons, ButtonsProps } from './Buttons'

interface FilterProps extends ButtonsProps {
    hideButtons?: boolean
    children: ReactNode
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

    return visible ? (
        <div className={classNames('n2o-filter', className)} style={style}>
            {children}

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
