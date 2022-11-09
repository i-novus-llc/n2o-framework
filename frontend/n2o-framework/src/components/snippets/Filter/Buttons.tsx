import React, { MouseEventHandler } from 'react'
import { ButtonGroup, Button } from 'reactstrap'
import { useTranslation } from 'react-i18next'

import { BaseComponentProps } from '../../types'

export interface ButtonsProps extends BaseComponentProps {
    searchLabel?: string
    resetLabel?: string
    onSearch: MouseEventHandler<HTMLButtonElement> | undefined
    onReset: MouseEventHandler<HTMLButtonElement> | undefined
}

export function Buttons(props: ButtonsProps) {
    const {
        className,
        visible = true,
        disabled,
        searchLabel,
        resetLabel,
        onSearch,
        onReset,
    } = props
    const { t } = useTranslation()

    return visible ? (
        <ButtonGroup className={className}>
            <Button disabled={disabled} color="primary" onClick={onSearch}>
                {searchLabel || t('search')}
            </Button>
            <Button disabled={disabled} color="secondary" onClick={onReset}>
                {resetLabel || t('reset')}
            </Button>
        </ButtonGroup>
    ) : null
}

export default Buttons
