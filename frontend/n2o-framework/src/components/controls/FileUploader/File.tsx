import classNames from 'classnames'
import React, { LegacyRef } from 'react'

export interface Props {
    forwardedRef?: LegacyRef<HTMLSpanElement>
    name: string
    link: string
    id: string
    error: string
}

export function File({ forwardedRef, name, link, id, error }: Props) {
    return (
        <a
            title={name}
            href={link}
            target="_blank"
            id={`tooltip-${id}`}
            className={classNames('n2o-file-uploader-link', { 'n2o-file-uploader-item-error': error })}
            rel="noreferrer"
        >
            <span ref={forwardedRef} className="n2o-file-uploader-file-name" title="">{name}</span>
            {link && <i className=" n2o-file-uploader-external-link fa fa-external-link" />}
        </a>
    )
}
