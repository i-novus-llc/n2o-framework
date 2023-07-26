import React, { MouseEventHandler } from 'react'
import { ButtonGroup, Button } from 'reactstrap'
import { useTranslation } from 'react-i18next'

import { TBaseProps } from '../../types'

export interface ButtonsProps extends Omit<TBaseProps, 'disabled'> {
    searchLabel?: string
    resetLabel?: string
    searchDisabled?: boolean
    clearDisabled?: boolean
    onSearch: MouseEventHandler<HTMLButtonElement> | undefined
    onReset: MouseEventHandler<HTMLButtonElement> | undefined
}

export function Buttons(props: ButtonsProps) {
    const {
        className,
        visible = true,
        searchLabel,
        resetLabel,
        searchDisabled,
        clearDisabled,
        onSearch,
        onReset,
    } = props
    const { t } = useTranslation()

    return visible ? (
        <ButtonGroup className={className}>
            <Button disabled={searchDisabled} color="primary" onClick={onSearch}>
                {searchLabel || t('search')}
            </Button>
            <Button disabled={clearDisabled} color="secondary" onClick={onReset}>
                {resetLabel || t('reset')}
            </Button>
        </ButtonGroup>
    ) : null
}
