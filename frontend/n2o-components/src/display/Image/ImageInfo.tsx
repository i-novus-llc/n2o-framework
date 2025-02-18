import React from 'react'

import { Text } from '../../Typography/Text'

export type Props = {
    description?: string,
    title?: string
}

export function ImageInfo({ title, description }: Props) {
    if (!title && !description) { return null }

    return (
        <section className="n2o-image__info">
            {title && <h4 className="n2o-image__info_label"><Text>{title}</Text></h4>}
            {description && <p className="n2o-image__info_description"><Text>{description}</Text></p>}
        </section>
    )
}
