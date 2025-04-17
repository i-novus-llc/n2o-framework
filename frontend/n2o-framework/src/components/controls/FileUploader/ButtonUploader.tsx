import React from 'react'
import classNames from 'classnames'
import { withTranslation } from 'react-i18next'
import flowRight from 'lodash/flowRight'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import withFileUploader from './withFileUploader'
import FileUploader from './FileUploader'

interface ButtonUploaderChildrenProps {
    children?: React.ReactNode
    icon: string
    label: string
    disabled: boolean
}

function ButtonUploaderChildren({
    children,
    icon,
    label,
    disabled,
}: ButtonUploaderChildrenProps) {
    return (
        <div className={classNames('n2o-button-uploader-btn btn btn-secondary', { disabled })}>
            {children || (
                <>
                    <div className={classNames('n2o-file-uploader-icon', { [icon]: icon })} />
                    <span><Text>{label}</Text></span>
                </>
            )}
        </div>
    )
}

interface ButtonUploaderProps {
    t(key: string): string
    children?: React.ReactNode
    icon?: string
    disabled?: boolean
    label?: string
    requestParam?: string
    visible?: boolean
    statusBarColor?: string
    multi?: boolean
    autoUpload?: boolean
    showSize?: boolean
    value?: unknown[]
    onChange?(value: unknown[]): void
}

function ButtonUploader({
    t,
    children,
    icon = 'fa fa-upload',
    disabled = false,
    label = t('uploadFile'),
    requestParam = 'file',
    visible = true,
    statusBarColor = 'success',
    multi = true,
    autoUpload = true,
    showSize = true,
    value = [],
    onChange = () => {},
    ...props
}: ButtonUploaderProps) {
    return (
        <FileUploader
            {...props}
            componentClass="n2o-button-uploader"
            disabled={disabled}
            requestParam={requestParam}
            visible={visible}
            statusBarColor={statusBarColor}
            multi={multi}
            autoUpload={autoUpload}
            showSize={showSize}
            value={value}
            onChange={onChange}
        >
            <ButtonUploaderChildren icon={icon} label={label} disabled={disabled}>
                {children}
            </ButtonUploaderChildren>
        </FileUploader>
    )
}

export default flowRight(
    withFileUploader,
    withTranslation(),
)(ButtonUploader)
