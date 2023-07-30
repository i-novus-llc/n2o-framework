import React, { MouseEventHandler } from 'react'
import { ButtonGroup, Button } from 'reactstrap'
import { useTranslation } from 'react-i18next'

import { TBaseProps } from '../../types'

export interface ButtonsProps extends Omit<TBaseProps, 'disabled'> {
    clearDisabled?: boolean,
    onReset: MouseEventHandler<HTMLButtonElement> | undefined,
    onSearch: MouseEventHandler<HTMLButtonElement> | undefined,
    resetLabel?: string,
    searchDisabled?: boolean,
    searchLabel?: string
}

export function Buttons({
    className,
    visible = true,
    searchLabel,
    resetLabel,
    searchDisabled,
    clearDisabled,
    onSearch,
    onReset,
}: ButtonsProps) {
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
