import React, { ReactNode, useCallback } from 'react'
import classNames from 'classnames'

import { Spinner, SpinnerType } from '../../layouts/Spinner/Spinner'

import { InputAddon } from './InputAddon'
import { TOption } from './types'

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
    className,
    loading,
    isExpanded,
    multiSelect,
    iconFieldId,
    imageFieldId,
    selected,
    input,
    cleanable,
    children,
    inputFocus,
    onClearClick,
    hidePopUp,
    disabled,
    setSelectedItemsRef,
    withoutButtons,
    onClick,
}: Props) {
    const clearClickHandler = useCallback((evt) => {
        evt.stopPropagation()
        onClearClick(evt)
    }, [onClearClick])

    const displayAddon = !multiSelect && !!selected?.length && (iconFieldId || imageFieldId)

    const renderButton = (loading: Props['loading']) => (
        <Spinner type={SpinnerType.inline} loading={loading}>
            <i
                onClick={hidePopUp}
                className="fa fa-chevron-down"
                aria-hidden="true"
            />
        </Spinner>
    )

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
                            className={classNames('n2o-input-clear', {
                                'input-in-focus': inputFocus,
                            })}
                            onClick={clearClickHandler}
                        >
                            <i className="fa fa-times" aria-hidden="true" />
                        </div>
                    )}
                    <div className={classNames('n2o-popup-control', { isExpanded })}>
                        {renderButton(loading)}
                    </div>
                </div>
            )}
        </div>
    )
}

type Props = {
    children: ReactNode,
    className: string,
    cleanable: boolean,
    disabled: boolean,
    hidePopUp(): void,
    iconFieldId: string,
    imageFieldId: string,
    input: ReactNode,
    inputFocus: ReactNode,
    isExpanded?: boolean,
    loading: boolean,
    multiSelect: boolean,
    onClearClick(evt: Event): void,
    onClick(): void,
    selected?: TOption[],
    setSelectedItemsRef?(): void,
    withoutButtons: boolean
}

InputSelectGroup.defaultProps = {
    cleanable: true,
    multiSelect: false,
    loading: false,
    withoutButtons: false,
    hidePopUp: () => {},
    onClick: () => {},
} as Props
