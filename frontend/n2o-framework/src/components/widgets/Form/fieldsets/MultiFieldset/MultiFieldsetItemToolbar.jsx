import React from 'react'
import { Button } from 'reactstrap'

/* FIXME сделать нормальный toolbar */
export function MultiFieldsetItemToolbar({
    needCopyButton,
    needRemoveButton,
    disabled,
    index,
    canRemoveFirstItem,
    onRemoveField,
    onCopyField,
}) {
    const onCopyClick = () => onCopyField(index)
    const onRemoveClick = () => onRemoveField(index)

    return (
        <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--inner">
            {needCopyButton && (
                <Button
                    className="n2o-multi-fieldset__copy"
                    color="link"
                    size="sm"
                    onClick={onCopyClick}
                    disabled={disabled}
                >
                    <i className="fa fa-copy" />
                </Button>
            )}
            {needRemoveButton && index > +!canRemoveFirstItem - 1 && (
                <Button
                    className="n2o-multi-fieldset__remove"
                    color="link"
                    size="sm"
                    onClick={onRemoveClick}
                    disabled={disabled}
                >
                    <i className="fa fa-trash" />
                </Button>
            )}
        </div>
    )
}
