import React, { ReactNode, useCallback, MouseEvent } from 'react'
import classNames from 'classnames'

import { Spinner, SpinnerType } from '../../layouts/Spinner/Spinner'
import { EMPTY_ARRAY, NOOP_FUNCTION } from '../../utils/emptyTypes'

import { InputAddon } from './InputAddon'
import { TOption } from './types'

interface Props {
    children: ReactNode
    className?: string
    cleanable?: boolean
    disabled?: boolean
    hidePopUp?(): void
    iconFieldId?: string
    imageFieldId?: string
    input?: ReactNode
    inputFocus?: boolean
    isExpanded?: boolean
    loading?: boolean
    multiSelect?: boolean
    onClearClick?(evt: MouseEvent): void,
    onClick?(): void
    selected?: TOption[]
    setSelectedItemsRef?(): void
    withoutButtons?: boolean
}

interface RenderButtonProps {
    loading: boolean
    hidePopUp(): void
}

const RenderButton = ({ loading, hidePopUp }: RenderButtonProps) => (
    <Spinner type={SpinnerType.inline} loading={loading}>
        <i
            onClick={hidePopUp}
            className="fa fa-chevron-down"
            aria-hidden="true"
        />
    </Spinner>
)

/**
 * InputSelectGroup
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {boolean} isExpanded - флаг видимости popUp
 * @reactProps {array} selected - список выбранных элементов
 * @reactProps {node} input
 * @reactProps {node} children - эдемент потомок компонента InputSelectGroup
 * @reactProps {boolean} isInputInFocus
 * @reactProps {boolean} disabled
 * @reactProps {function} onClearClick
 * @reactProps {function} setIsExpanded
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {boolean} cleanable - показывать иконку очисть поле
 * @reactProps {boolean} multiSelect - флаг мульти выбора
 * @reactProps {boolean} withoutButtons - флаг скрытия кнопок действий
 */

export function InputSelectGroup({
    className = '',
    loading = false,
    isExpanded = false,
    multiSelect = false,
    iconFieldId = '',
    imageFieldId = '',
    selected = EMPTY_ARRAY,
    input = null,
    cleanable = true,
    children,
    inputFocus = false,
    onClearClick = NOOP_FUNCTION,
    hidePopUp = NOOP_FUNCTION,
    disabled = false,
    setSelectedItemsRef,
    withoutButtons = false,
    onClick = NOOP_FUNCTION,
}: Props) {
    const clearClickHandler = useCallback((evt: MouseEvent<HTMLElement>) => {
        evt.stopPropagation()
        onClearClick(evt)
    }, [onClearClick])

    const displayAddon = !multiSelect && !!selected?.length && (iconFieldId || imageFieldId)

    return (
        <div
            className={classNames('n2o-input-container', 'form-control', className, { disabled })}
            onClick={onClick}
        >
            <div className="n2o-input-items">
                {displayAddon && (
                    <InputAddon
                        item={selected ? selected[0] : null}
                        imageFieldId={imageFieldId}
                        iconFieldId={iconFieldId}
                        setRef={setSelectedItemsRef}
                    />
                )}
                {children}
            </div>
            {!withoutButtons && !disabled && (
                <div className="n2o-input-control">
                    {(selected?.length || input) && cleanable && (
                        <div
                            className={classNames('n2o-input-clear', { 'input-in-focus': inputFocus })}
                            onClick={clearClickHandler}
                        >
                            <i className="fa fa-times" aria-hidden="true" />
                        </div>
                    )}
                    <div className={classNames('n2o-popup-control', { isExpanded })}>
                        <RenderButton loading={loading} hidePopUp={hidePopUp} />
                    </div>
                </div>
            )}
        </div>
    )
}

InputSelectGroup.displayName = 'InputSelectGroup'
