import React, { ReactNode, CSSProperties } from 'react'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import { FileUploaderControl } from './withFileUploader'
import { FileUploader } from './FileUploader'
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
    children?: ReactNode
    saveBtnStyle?: CSSProperties
}

function DropZone({
    icon,
    label,
    children,
    saveBtnStyle = { marginTop: '10px' },
    ...props
}: Props) {
    return (
        <FileUploader
            {...props}
            componentClass="n2o-drop-zone"
            saveBtnStyle={saveBtnStyle}
        >
            {children ? <Text>{children}</Text> : defaultDropZone(icon, label)}
        </FileUploader>
    )
}

export default FileUploaderControl<Props>(DropZone)
