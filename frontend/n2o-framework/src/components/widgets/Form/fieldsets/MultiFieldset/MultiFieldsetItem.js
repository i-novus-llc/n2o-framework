import React from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import map from 'lodash/map'
import Button from 'reactstrap/lib/Button'

export function MultiFieldsetItem({
    fields,
    render,
    rows,
    childrenLabel,
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
}) {
    return (
        <>
            {map(fields, (field, index) => (
                <div className="n2o-multi-fieldset__container">
                    <div className="n2o-multi-fieldset__item">
                        {childrenLabel && (
                            <div className="n2o-multi-fieldset__label">
                                {resolvePlaceholder(index)}
                            </div>
                        )}
                        {render(rows, {
                            parentName: `${parentName}[${index}]`,
                            parentIndex: index,
                        })}
                        <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--inner">
                            {needCopyButton && (
                                <Button
                                    className="n2o-multi-fieldset__copy"
                                    color="link"
                                    size="sm"
                                    onClick={onCopyField(index)}
                                    disabled={!enabled}
                                >
                                    <i className="fa fa-copy" />
                                </Button>
                            )}
                            {needRemoveButton && index > +!canRemoveFirstItem - 1 && (
                                <Button
                                    className="n2o-multi-fieldset__remove"
                                    color="link"
                                    size="sm"
                                    onClick={onRemoveField(index)}
                                    disabled={!enabled}
                                >
                                    <i className="fa fa-trash" />
                                </Button>
                            )}
                        </div>
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
    childrenLabel: PropTypes.string,
    addButtonLabel: PropTypes.string,
    removeButtonLabel: PropTypes.string,
    removeAllButtonLabel: PropTypes.string,
    copyButtonLabel: PropTypes.string,
    needAddButton: PropTypes.bool,
    needRemoveButton: PropTypes.bool,
    needRemoveAllButton: PropTypes.bool,
    needCopyButton: PropTypes.bool,
    canRemoveFirstItem: PropTypes.bool,
}

const defaultComponentProps = {
    render: () => {},
    rows: [],
    fields: [],
    childrenLabel: null,
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
