import React from 'react'

type fieldAlignmentBlockType = {
    visible?: boolean
}

export function FieldAlignmentBlock({ visible = true }: fieldAlignmentBlockType) {
    if (!visible) {
        return null
    }

    return <div className="n2o-field-alignment" />
}
