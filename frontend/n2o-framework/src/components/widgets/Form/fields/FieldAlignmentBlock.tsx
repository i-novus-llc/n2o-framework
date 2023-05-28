import React from 'react'

type fieldAlignmentBlockType = {
    visible?: boolean
}

export function FieldAlignmentBlock(props: fieldAlignmentBlockType) {
    const { visible = true } = props

    if (!visible) {
        return null
    }

    return <div className="n2o-field-alignment" />
}
