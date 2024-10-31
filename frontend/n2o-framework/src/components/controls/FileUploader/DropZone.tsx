import React, { ReactNode } from 'react'

import withFileUploader from './withFileUploader'
import FileUploader from './FileUploader'

function defaultDropZone(icon: string, label: string) {
    return (
        <>
            <div className={icon} />
            {label}
        </>
    )
}

export type Props = {
    icon: string
    label: string
    children: ReactNode
}

function DropZone(props: Props) {
    const { icon, label, children } = props

    return (
        // @ts-ignore ignore import error from js file
        <FileUploader
            {...props}
            componentClass="n2o-drop-zone"
        >
            {children || defaultDropZone(icon, label)}
        </FileUploader>
    )
}

DropZone.defaultProps = {
    saveBtnStyle: {
        marginTop: '10px',
    },
}

/**
 * @type ReturnedComponent
 */
export default withFileUploader(DropZone)
