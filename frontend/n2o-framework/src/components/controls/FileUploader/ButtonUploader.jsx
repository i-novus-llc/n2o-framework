import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { withTranslation } from 'react-i18next'
import { compose } from 'recompose'

import withFileUploader from './withFileUploader'
import FileUploader from './FileUploader'

function ButtonUploaderChildren(props) {
    const { children, icon, label, disabled } = props

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

function ButtonUploader(props) {
    const { t, children, icon, disabled = false, label = t('uploadFile') } = props

    return (
        <FileUploader {...props} componentClass="n2o-button-uploader">
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

ButtonUploader.propTypes = {
    t: PropTypes.func,
    /**
     * Ключ ID из даныых
     */
    valueFieldId: PropTypes.string,
    /**
     * Ключ label из данных
     */
    labelFieldId: PropTypes.string,
    /**
     * Ключ status из данных
     */
    statusFieldId: PropTypes.string,
    /**
     * Ключ size из данных
     */
    sizeFieldId: PropTypes.string,
    /**
     * Ключ response из даннах
     */
    responseFieldId: PropTypes.string,
    /**
     * Ключ url из данных
     */
    urlFieldId: PropTypes.string,
    /**
     * Url для загрузки файла
     */
    uploadUrl: PropTypes.string,
    /**
     * Url для удаления файла
     */
    deleteUrl: PropTypes.string,
    /**
     * Флаг мульти выбора файлов
     */
    multi: PropTypes.bool,
    /**
     * Массив файлов
     */
    files: PropTypes.arrayOf(PropTypes.object),
    /**
     * Значение
     */
    value: PropTypes.arrayOf(PropTypes.object),
    /**
     * Флаг автоматической загрузки файлов после выбора
     */
    autoUpload: PropTypes.bool,
    /**
     * Максимальный размер файла
     */
    maxSize: PropTypes.number,
    /**
     * Минимальный размер файла
     */
    minSize: PropTypes.number,
    /**
     * Label контрола
     */
    label: PropTypes.string,
    /**
     * Название отправлякмого параметра
     */
    requestParam: PropTypes.string,
    /**
     * Флаг видимости
     */
    visible: PropTypes.bool,
    /**
     * Флаг активности
     */
    disabled: PropTypes.bool,
    /**
     * Иконка рядом с label
     */
    icon: PropTypes.string,
    /**
     * Цвет статус бара
     */
    statusBarColor: PropTypes.string,
    /**
     * Объект стилей кнопки 'Сохранить'
     */
    saveBtnStyle: PropTypes.object,
    /**
     * Флаг показа размера файла
     */
    showSize: PropTypes.bool,
    /**
     * Callback на изменение
     */
    onChange: PropTypes.func,
    /**
     * Класс контрола
     */
    className: PropTypes.string,
    /**
     * Mapper значения
     */
    mapper: PropTypes.func,
    children: PropTypes.oneOfType([PropTypes.func, PropTypes.node]),
    errorFormatter: PropTypes.func,
}

export default compose(
    withFileUploader,
    withTranslation(),
)(ButtonUploader)
