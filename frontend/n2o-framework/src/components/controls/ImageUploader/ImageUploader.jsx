import React from 'react'
import PropTypes from 'prop-types'
import { compose } from 'recompose'
import { withTranslation } from 'react-i18next'

import withFileUploader from '../FileUploader/withFileUploader'

import ImageUpload from './ImageUpload'
import {
    createCustomSizes,
    getSize,
    getCurrentLabel,
    defaultDropZone,
} from './utils'

/**
 * Компонент ImageUploader
 * @param props - иконка в dropzone uploader
 * @param icon - иконка в dropzone uploader
 * @param label - label в dropzone
 * @param children - children
 * @param showTooltip - флаг на резолв тултипа со статусом загрузки
 * @param imgError - ошибка загрузки
 * @param width - кастом ширина
 * @param height - кастом высота
 * @param iconSize - кастом размер иконки
 * @param unit - еденицы измерения для кастом размеров (px default)
 * @param canDelete - отключает возможность удаления
 * @param shape -мод в котором можно указать форму uploader (circle)
 */

function ImageUploader({
    icon,
    label,
    children,
    showTooltip,
    imgError,
    width,
    height,
    iconSize,
    unit,
    canDelete,
    shape,
    ...rest
}) {
    const currentLabel = getCurrentLabel(imgError, label)
    const size = createCustomSizes(width, height, iconSize, unit)
    const component = children || defaultDropZone(icon, currentLabel, size)

    return (
        <ImageUpload
            children={component}
            componentClass="n2o-drop-zone"
            customUploaderSize={getSize(size, 'uploader')}
            icon={icon}
            label={label}
            showTooltip={showTooltip}
            imgError={imgError}
            width={width}
            height={height}
            iconSize={iconSize}
            canDelete={canDelete}
            shape={shape}
            {...rest}
        />
    )
}

ImageUpload.defaultProps = {
    showTooltip: true,
    canDelete: true,
    lightbox: false,
    accept: 'image/*',
}

ImageUploader.propTypes = {
    label: PropTypes.string,
    uploading: PropTypes.object,
    icon: PropTypes.string,
    files: PropTypes.arrayOf(PropTypes.object),
    className: PropTypes.string,
    onDrop: PropTypes.func,
    autoUpload: PropTypes.bool,
    onRemove: PropTypes.func,
    onStartUpload: PropTypes.func,
    saveBtnStyle: PropTypes.object,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
    requestParam: PropTypes.string,
    maxSize: PropTypes.number,
    minSize: PropTypes.number,
    multiple: PropTypes.bool,
    onChange: PropTypes.func,
    children: PropTypes.oneOfType([PropTypes.node, PropTypes.func]),
    avatar: PropTypes.bool,
    width: PropTypes.number,
    height: PropTypes.number,
    showTooltip: PropTypes.bool,
    canDelete: PropTypes.bool,
    shape: PropTypes.string,
}

export default compose(
    withTranslation(),
    withFileUploader,
)(ImageUploader)
