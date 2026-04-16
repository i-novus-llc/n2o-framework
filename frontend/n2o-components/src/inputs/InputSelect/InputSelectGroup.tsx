import React, { ReactNode, useCallback, MouseEvent, ReactElement } from 'react'
import classNames from 'classnames'

import { Spinner, SpinnerType } from '../../layouts/Spinner/Spinner'
import { EMPTY_ARRAY, EMPTY_OBJECT, NOOP_FUNCTION } from '../../utils/emptyTypes'
import { Icon } from '../../display/Icon'
import { type InputElementsProps, ShowClearTrigger, DefaultIcons, getShowClearTriggerClass } from '../inputElements/inputElements'

import { InputAddon } from './InputAddon'
import { type TOption } from './types'

interface Props {
    children: ReactNode
    className?: string
    cleanable?: boolean
    disabled?: boolean
    hidePopUp?(): void
    iconFieldId?: string
    imageFieldId?: string
    input?: ReactNode
    inputElements?: InputElementsProps
    inputFocus?: boolean
    isExpanded?: boolean
    loading?: boolean
    multiSelect?: boolean
    onClearClick?(evt: MouseEvent): void
    onClick?(): void
    selected?: TOption[]
    setSelectedItemsRef?(): void
    showClearTrigger?: ShowClearTrigger
    withoutButtons?: boolean
}

interface RenderButtonProps {
    loading: boolean
    hidePopUp(): void
    spinner?: ReactNode | { color?: string }
    buttons?: InputElementsProps['buttons']
    isExpanded?: boolean
}

const RenderButton = ({ loading, hidePopUp, spinner, buttons, isExpanded }: RenderButtonProps): ReactElement | null => {
    if (loading) {
        if (spinner) {
            if (React.isValidElement(spinner)) {
                return spinner as ReactElement
            }
            if (typeof spinner === 'object' && 'color' in spinner) {
                return <Spinner className="input-select-loader" type={SpinnerType.cover} color={spinner.color} loading />
            }
        }

        return <Spinner className="input-select-loader" type={SpinnerType.cover} loading />
    }

    const toggleButton: ReactNode | string = isExpanded ? buttons?.up : buttons?.down

    if (toggleButton) {
        if (typeof toggleButton === 'string') {
            return (
                <i
                    onClick={hidePopUp}
                    className={toggleButton}
                    aria-hidden="true"
                />
            )
        }

        if (React.isValidElement(toggleButton)) {
            return toggleButton as ReactElement
        }
    }

    return (
        <i
            onClick={hidePopUp}
            className="fa fa-chevron-down"
            aria-hidden="true"
        />
    )
}

/**
 * InputSelectGroup
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {boolean} isExpanded - флаг видимости popUp
 * @reactProps {array} selected - список выбранных элементов
 * @reactProps {node} input
 * @reactProps {node} children - элемент потомок компонента InputSelectGroup
 * @reactProps {boolean} isInputInFocus
 * @reactProps {boolean} disabled
 * @reactProps {function} onClearClick
 * @reactProps {function} setIsExpanded
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {boolean} cleanable - показывать иконку очистки поля
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
    showClearTrigger = ShowClearTrigger.HOVER,
    withoutButtons = false,
    onClick = NOOP_FUNCTION,
    inputElements = EMPTY_OBJECT,
}: Props) {
    const clearClickHandler = useCallback((evt: MouseEvent<HTMLElement>) => {
        evt.stopPropagation()
        onClearClick(evt)
    }, [onClearClick])

    const displayAddon = !multiSelect && !!selected?.length && (iconFieldId || imageFieldId)

    const { spinner, buttons = {} } = inputElements
    const { clear = DefaultIcons.CLEAR } = buttons

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
                    {!loading && (selected?.length || input) && cleanable && (
                        <div
                            className={classNames('n2o-input-clear', { 'input-in-focus': inputFocus })}
                        >
                            <Icon
                                name={clear}
                                className={getShowClearTriggerClass(showClearTrigger)}
                                onClick={clearClickHandler}
                            />
                        </div>
                    )}
                    <div className={classNames('n2o-popup-control', { isExpanded })}>
                        <RenderButton
                            loading={loading}
                            hidePopUp={hidePopUp}
                            spinner={spinner}
                            buttons={buttons}
                            isExpanded={isExpanded}
                        />
                    </div>
                </div>
            )}
        </div>
    )
}

InputSelectGroup.displayName = 'InputSelectGroup'
