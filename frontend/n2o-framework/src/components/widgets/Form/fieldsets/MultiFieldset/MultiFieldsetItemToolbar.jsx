import React from 'react'
import PropTypes from 'prop-types'
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
    return (
        <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--inner">
            {needCopyButton && (
                <Button
                    className="n2o-multi-fieldset__copy"
                    color="link"
                    size="sm"
                    onClick={onCopyField(index)}
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
                    onClick={onRemoveField(index)}
                    disabled={disabled}
                >
                    <i className="fa fa-trash" />
                </Button>
            )}
        </div>
    )
}

MultiFieldsetItemToolbar.propTypes = {
    needCopyButton: PropTypes.bool,
    needRemoveButton: PropTypes.bool,
    disabled: PropTypes.bool,
    index: PropTypes.number,
    canRemoveFirstItem: PropTypes.bool,
    onRemoveField: PropTypes.func,
    onCopyField: PropTypes.func,
}
