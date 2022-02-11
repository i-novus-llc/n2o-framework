import React, { useCallback } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { Spinner } from '../../snippets/Spinner/Spinner'

import InputAddon from './InputAddon'

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

function InputSelectGroup({
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
}) {
    const clearClickHandler = useCallback((evt) => {
        evt.stopPropagation()
        onClearClick(evt)
    }, [onClearClick])

    const displayAddon = !multiSelect && !!selected.length && (iconFieldId || imageFieldId)

    const renderButton = loading => (
        <Spinner type="inline" loading={loading} size="sm">
            <i
                onClick={hidePopUp}
                className="fa fa-chevron-down"
                aria-hidden="true"
            />
        </Spinner>
    )

    return (
        <div
            className={classNames(
                'n2o-input-container',
                'form-control',
                className, {
                    disabled,
                },
            )}
        >
            <div className="n2o-input-items">
                {displayAddon && (
                    <InputAddon
                        item={selected[0]}
                        imageFieldId={imageFieldId}
                        iconFieldId={iconFieldId}
                        setRef={setSelectedItemsRef}
                    />
                )}
                {children}
            </div>
            {!withoutButtons && !disabled && (
                <div className="n2o-input-control">
                    {(selected.length || input) && cleanable && (
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

InputSelectGroup.propTypes = {
    loading: PropTypes.bool,
    isExpanded: PropTypes.bool.isRequired,
    selected: PropTypes.array.isRequired,
    input: PropTypes.node,
    children: PropTypes.node,
    inputFocus: PropTypes.node,
    iconFieldId: PropTypes.string,
    imageFieldId: PropTypes.string,
    multiSelect: PropTypes.bool,
    disabled: PropTypes.bool,
    onClearClick: PropTypes.func,
    hidePopUp: PropTypes.func,
    cleanable: PropTypes.bool,
    withoutButtons: PropTypes.bool,
    setSelectedItemsRef: PropTypes.func,
    className: PropTypes.string,
}

InputSelectGroup.defaultProps = {
    cleanable: true,
    multiSelect: false,
    loading: false,
    withoutButtons: false,
    hidePopUp: () => {},
}

export default InputSelectGroup
