import React from 'react'

export type Props = {
    description?: string,
    title?: string
}

export function ImageInfo({ title, description }: Props) {
    if (!title && !description) { return null }

    return (
        <section className="n2o-image__info">
            {title && <h4 className="n2o-image__info_label">{title}</h4>}
            {description && <p className="n2o-image__info_description">{description}</p>}
        </section>
    )
}
