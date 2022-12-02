import React from 'react'
import classNames from 'classnames'

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

    return visible ? (
        <div className={classNames('n2o-filter', className)} style={style}>
            {children}

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
