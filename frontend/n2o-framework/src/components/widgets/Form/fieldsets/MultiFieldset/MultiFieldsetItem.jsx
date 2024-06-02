import React from 'react'
import classNames from 'classnames'
import PropTypes from 'prop-types'

import { useResolved } from '../../../../../core/Expression/useResolver'

import { MultiFieldsetItemToolbar } from './MultiFieldsetItemToolbar'

export function MultiFieldsetItem({
    canRemoveFirstItem,
    disabled: propsDisabled,
    enabled,
    index,
    label: propsLabel,
    model,
    needCopyButton: needToCopyButtonExpression,
    needRemoveButton: needToRemoveButtonExpression,
    onCopyField,
    onRemoveField,
    parentName,
    render,
    rows,
}) {
    const disabled = propsDisabled || !enabled
    const label = useResolved(propsLabel, model)

    const isNeedToCopyButton = useResolved(needToCopyButtonExpression, model)
    const isNeedToRemoveButton = useResolved(needToRemoveButtonExpression, model)

    return (
        <div className="n2o-multi-fieldset__container">
            <div className="n2o-multi-fieldset__item">
                <section className="n2o-multi-fieldset__item-top-section">
                    <div className={classNames('n2o-multi-fieldset__label', { empty: !label })}>
                        {label}
                    </div>
                    <MultiFieldsetItemToolbar
                        needCopyButton={isNeedToCopyButton}
                        needRemoveButton={isNeedToRemoveButton}
                        disabled={!enabled}
                        index={index}
                        canRemoveFirstItem={canRemoveFirstItem}
                        onRemoveField={onRemoveField}
                        onCopyField={onCopyField}
                    />
                </section>
                {render(rows, {
                    parentName: `${parentName}[${index}]`,
                    multiSetDisabled: disabled,
                })}
            </div>
        </div>
    )
}

MultiFieldsetItem.propTypes = {
    canRemoveFirstItem: PropTypes.bool,
    enabled: PropTypes.bool,
    index: PropTypes.number,
    label: PropTypes.string,
    model: PropTypes.any,
    needCopyButton: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
    needRemoveButton: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
    onCopyField: PropTypes.func,
    onRemoveField: PropTypes.func,
    parentName: PropTypes.string,
    render: PropTypes.func,
    rows: PropTypes.array,
}

const defaultComponentProps = {
    canRemoveFirstItem: false,
    needCopyButton: false,
    needRemoveButton: true,
    render: () => {},
    rows: [],
}

MultiFieldsetItem.defaultProps = defaultComponentProps
