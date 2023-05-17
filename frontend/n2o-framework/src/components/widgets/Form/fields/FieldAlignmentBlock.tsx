import React from 'react'

export function FieldAlignmentBlock({
    visible,
}: { visible: boolean }): null | JSX.Element {
    if (!visible) {
        return null
    }

    return <div className="n2o-field-alignment" />
}
