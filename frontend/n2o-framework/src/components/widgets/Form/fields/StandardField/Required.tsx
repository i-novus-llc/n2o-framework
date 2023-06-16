import React from 'react'

export function Required({ required }: {required: boolean}) {
    if (!required) {
        return null
    }

    return <span className="n2o-field-label-required">*</span>
}
