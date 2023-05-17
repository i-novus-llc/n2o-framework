import React from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import map from 'lodash/map'
import { Button } from 'reactstrap'

import { MultiFieldsetItemToolbar } from './MultiFieldsetItemToolbar'

function MultiFieldsetItem({
    fields,
    render,
    rows,
    parentName,
    addButtonLabel,
    removeAllButtonLabel,
    needAddButton,
    needRemoveButton,
    needRemoveAllButton,
    needCopyButton,
    canRemoveFirstItem,
    resolvePlaceholder,
    onAddField,
    onRemoveField,
    onRemoveAll,
    onCopyField,
    enabled,
    disabled: propsDisabled,
}) {
    const disabled = propsDisabled || !enabled

    return (
        <>
            {map(fields, (field, index) => (
                <div className="n2o-multi-fieldset__container">
                    <div className="n2o-multi-fieldset__item">
                        <section className="n2o-multi-fieldset__item-top-section">
                            <div className="n2o-multi-fieldset__label">
                                {resolvePlaceholder(index)}
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
                            parentIndex: index,
                            multiSetDisabled: disabled,
                        })}
                    </div>
                </div>
            ))}
            <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--common">
                {needAddButton && (
                    <Button
                        className="n2o-multi-fieldset__add"
                        onClick={onAddField}
                        disabled={!enabled}
                    >
                        <i className="fa fa-plus mr-1" />
                        {addButtonLabel}
                    </Button>
                )}
                {!isEmpty(fields) && needRemoveAllButton && (
                    <Button
                        className="n2o-multi-fieldset__remove-all"
                        onClick={onRemoveAll}
                        disabled={!enabled}
                    >
                        <i className="fa fa-trash mr-1" />
                        {removeAllButtonLabel}
                    </Button>
                )}
            </div>
        </>
    )
}

MultiFieldsetItem.propTypes = {
    fields: PropTypes.object,
    render: PropTypes.func,
    rows: PropTypes.array,
    addButtonLabel: PropTypes.string,
    removeAllButtonLabel: PropTypes.string,
    needAddButton: PropTypes.bool,
    needRemoveButton: PropTypes.bool,
    needRemoveAllButton: PropTypes.bool,
    needCopyButton: PropTypes.bool,
    canRemoveFirstItem: PropTypes.bool,
    parentName: PropTypes.string,
    resolvePlaceholder: PropTypes.func,
    onAddField: PropTypes.func,
    onRemoveField: PropTypes.func,
    onRemoveAll: PropTypes.func,
    onCopyField: PropTypes.func,
    enabled: PropTypes.bool,
}

const defaultComponentProps = {
    render: () => {},
    rows: [],
    fields: [],
    addButtonLabel: 'Добавить',
    removeAllButtonLabel: 'Удалить все',
    needAddButton: true,
    needRemoveButton: true,
    needRemoveAllButton: false,
    needCopyButton: false,
    canRemoveFirstItem: false,
}

MultiFieldsetItem.defaultProps = defaultComponentProps

export default MultiFieldsetItem
