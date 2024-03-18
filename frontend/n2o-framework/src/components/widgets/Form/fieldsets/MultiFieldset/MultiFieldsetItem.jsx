import React from 'react'
import classNames from 'classnames'
import PropTypes from 'prop-types'

import { useResolved } from '../../../../../core/Expression/useResolver'

import { MultiFieldsetItemToolbar } from './MultiFieldsetItemToolbar'

export function MultiFieldsetItem({
    index,
    render,
    rows,
    parentName,
    needRemoveButton,
    needCopyButton,
    canRemoveFirstItem,
    onRemoveField,
    onCopyField,
    enabled,
    disabled: propsDisabled,
    label: propsLabel,
    model,
}) {
    const disabled = propsDisabled || !enabled
    const label = useResolved(propsLabel, model)

    return (
        <div className="n2o-multi-fieldset__container">
            <div className="n2o-multi-fieldset__item">
                <section className="n2o-multi-fieldset__item-top-section">
                    <div className={classNames('n2o-multi-fieldset__label', { empty: !label })}>
                        {label}
                    </div>
                    <MultiFieldsetItemToolbar
                        needCopyButton={needCopyButton}
                        needRemoveButton={needRemoveButton}
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
    index: PropTypes.number,
    render: PropTypes.func,
    rows: PropTypes.array,
    needRemoveButton: PropTypes.bool,
    needCopyButton: PropTypes.bool,
    canRemoveFirstItem: PropTypes.bool,
    parentName: PropTypes.string,
    label: PropTypes.string,
    onRemoveField: PropTypes.func,
    onCopyField: PropTypes.func,
    enabled: PropTypes.bool,
    model: PropTypes.any,
}

const defaultComponentProps = {
    render: () => {},
    rows: [],
    needCopyButton: false,
    canRemoveFirstItem: false,
}

MultiFieldsetItem.defaultProps = defaultComponentProps
