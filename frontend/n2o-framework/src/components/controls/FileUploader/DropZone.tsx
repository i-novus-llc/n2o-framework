import React, { ReactNode } from 'react'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import withFileUploader from './withFileUploader'
import FileUploader from './FileUploader'
import { type FileUploaderProps } from './types'

function defaultDropZone(icon: string, label: string) {
    return (
        <>
            <div className={icon} />
            <Text>{label}</Text>
        </>
    )
}

export interface Props extends FileUploaderProps {
    icon: string
    label: string
    children: ReactNode
}

function DropZone(props: Props) {
    const { icon, label, children } = props

    return (
        <FileUploader
            {...props}
            componentClass="n2o-drop-zone"
        >
            {
                children
                    ? (<Text>{children}</Text>)
                    : defaultDropZone(icon, label)
            }
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
export default withFileUploader<Props>(DropZone)
