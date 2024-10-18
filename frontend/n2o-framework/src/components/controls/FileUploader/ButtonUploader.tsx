import React from 'react'
import classNames from 'classnames'
import { withTranslation } from 'react-i18next'
import { compose } from 'recompose'

import withFileUploader from './withFileUploader'
import FileUploader from './FileUploader'

interface ButtonUploaderChildrenProps {
    children?: React.ReactNode;
    icon: string;
    label: string;
    disabled: boolean;
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
                    <span>{label}</span>
                </>
            )}
        </div>
    )
}

interface ButtonUploaderProps {
    t(key: string): string;
    children?: React.ReactNode;
    icon: string;
    disabled?: boolean;
    label?: string;
    requestParam?: string;
    visible?: boolean;
    statusBarColor?: string;
    multi?: boolean;
    autoUpload?: boolean;
    showSize?: boolean;
    value?: unknown[];
    onChange?(value: unknown[]): void;
}

function ButtonUploader({
    t,
    children,
    icon,
    disabled = false,
    label = t('uploadFile'),
    ...props
}: ButtonUploaderProps) {
    return (
        // @ts-ignore ignore import error from js file
        <FileUploader {...props} componentClass="n2o-button-uploader" disabled={disabled}>
            <ButtonUploaderChildren icon={icon} label={label} disabled={disabled}>
                {children}
            </ButtonUploaderChildren>
        </FileUploader>
    )
}

ButtonUploader.defaultProps = {
    requestParam: 'file',
    visible: true,
    icon: 'fa fa-upload',
    statusBarColor: 'success',
    multi: true,
    disabled: false,
    autoUpload: true,
    showSize: true,
    value: [],
    onChange: () => {},
}

export default compose(
    withFileUploader,
    withTranslation(),
    // @ts-ignore ignore import error from js file
)(ButtonUploader)
