import React from 'react'

type Props = {
    title: string,
    description: string,
}

export function ImageInfo({ title, description }: Props) {
    return (
        <section className="n2o-image__info">
            {title && <h4 className="n2o-image__info_label">{title}</h4>}
            {description && (
                <p className="n2o-image__info_description">{description}</p>
            )}
        </section>
    )
}
