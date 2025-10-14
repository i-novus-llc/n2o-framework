import React, { Component, ComponentType } from 'react'
import axios, { AxiosResponse } from 'axios'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import isArray from 'lodash/isArray'
import reduce from 'lodash/reduce'
import every from 'lodash/every'
import some from 'lodash/some'
import get from 'lodash/get'
import isFunction from 'lodash/isFunction'
import has from 'lodash/has'
import find from 'lodash/find'

import { WithPropsResolver } from '../../../core/Expression/withResolver'
import { parseExpression } from '../../../core/Expression/parse'
import { id } from '../../../utils/id'
import { logger } from '../../../core/utils/logger'

import { deleteFile, everyIsValid, post } from './utils'
import {
    type FileUploaderControlProps,
    type FileUploaderControlState,
    type Files,
    type FileItem,
    type FileItemKeys,
} from './types'

export type UploaderError = { message: string, status?: string | number }

export function FileUploaderControl<P>(WrappedComponent: ComponentType<P>) {
    class ReturnedComponent extends Component<FileUploaderControlProps & P, FileUploaderControlState> {
        requests: Record<string, unknown> = {}

        static defaultProps: {
            autoUpload: boolean;
            disabled: boolean;
            icon: string;
            multi: boolean;
            onBlur(): void;
            onChange(): void;
            onDelete(): void;
            onStart(): void;
            onSuccess(): void;
            requestParam: string;
            showSize: boolean;
            sizeFieldId: string;
            statusBarColor: string;
            t(): void;
            value: never[];
            visible: boolean
        }

        constructor(props: FileUploaderControlProps & P) {
            super(props)

            this.state = {
                files: props.files || [],
                imgFiles: [],
                imgError: {},
            }
        }

        componentDidMount() {
            const { mapper, value, model, fieldKey } = this.props
            const { files } = this.state

            this.setState({ files: mapper ? mapper(value) : this.mapFiles(!isEmpty(value) ? value : files) as Files })

            if (isEmpty(value) && model) {
                const files = model[fieldKey] || []

                this.setState({
                    files: Array.isArray(files)
                        ? this.mapFiles(files) as Files
                        : this.mapFiles([files] as Files) as Files,
                })
            }
        }

        componentDidUpdate(prevProps: FileUploaderControlProps) {
            const { value, files, mapper } = this.props
            const { files: stateFiles } = this.state

            if (!isEqual(prevProps.value, value)) {
                if (!value) { this.setState({ files: [] }) }

                const newFiles = mapper ? mapper(value || []) : this.mapFiles(value || []) as Files

                const hasUpdate = !every(newFiles, file => some(stateFiles, file))

                if (hasUpdate) {
                    this.setState({ files: newFiles })
                }
            } else if (!isEqual(prevProps.files, files)) {
                this.setState({
                    files: mapper ? mapper(files || []) : this.mapFiles(files || []) as Files,
                })
            }
        }

        mapFiles = (files?: Files) => {
            if (!files) {
                return []
            }

            const currentFiles = isArray(files) ? files : [files]

            return currentFiles.map(file => this.fileAdapter(file))
        }

        fileAdapter = (file: FileItem) => {
            const {
                valueFieldId,
                labelFieldId,
                statusFieldId,
                sizeFieldId = 'size',
                responseFieldId,
                urlFieldId,
            } = this.props as FileItemKeys

            return {
                id: file[valueFieldId],
                name: file[labelFieldId] || file.fileName,
                status: file[statusFieldId],
                size: file[sizeFieldId],
                response: file[responseFieldId],
                link: file[urlFieldId],
            }
        }

        resolveUrl = (url: string) => {
            if (!parseExpression(url)) { return url }

            const { propsResolver, model } = this.props

            return propsResolver(url, model)
        }

        getUploaderProps = () => {
            const { files, imgFiles, uploading, uploaderClass, imgError } = this.state
            const { multi } = this.props

            return {
                ...this.props,
                files,
                imgFiles,
                uploading,
                requests: this.requests,
                multiple: multi,
                uploaderClass,
                onFocus: () => {},
                onBlur: () => {},
                onDrop: this.handleDrop,
                onDropRejected: this.onDropRejected,
                onImagesDrop: this.handleImagesDrop,
                onDragLeave: this.onDragLeave,
                onDragEnter: this.onDragEnter,
                onRemove: this.handleRemove,
                onStartUpload: this.onStartUpload,
                imgError,
            }
        }

        handleDrop = (data: Files) => {
            const { onChange, autoUpload, onBlur, multi } = this.props
            const { files: stateFiles } = this.state
            const preparedFiles = data?.map((file) => {
                file.id = id()
                file.percentage = 0

                return file
            })
            const files = multi ? [...stateFiles, ...preparedFiles] : preparedFiles

            this.setState(
                {
                    files,
                    uploaderClass: null,
                },
                () => {
                    if (autoUpload) {
                        this.startUpload(data)
                    } else {
                        onChange(data)
                        onBlur(data)
                    }
                },
            )
        }

        onDropRejected = (files: Files) => {
            const { accept } = this.props
            const { files: stateFiles } = this.state
            const errorText = `Ошибка формата файла. Ожидаемый тип: ${accept}`

            this.setState({
                files: [...stateFiles, ...files.map((file) => {
                    file.error = errorText
                    file.id = id()
                    file.percentage = 0

                    return file
                })],
            })
        }

        handleImagesDrop = (files: Files) => {
            const { accept, t } = this.props
            let errorText = `${t?.('imageUploadAvailableImageTypes')} JPG/PNG/SVG`

            if (accept) {
                errorText = `${t?.('imageUploadAvailableImageTypes')} ${accept}`
            }

            if (everyIsValid(files) && files?.length !== 0) {
                this.setState({
                    imgError: {},
                })

                const { imgFiles } = this.state

                this.setState({
                    imgFiles: imgFiles.concat(files),
                })

                this.handleDrop(files)
            } else {
                this.setState({
                    imgError: {
                        message: errorText,
                    },
                })
            }
        }

        handleRemove = async (index: number, id: string) => {
            const {
                value = [],
                multi,
                valueFieldId,
                onChange,
                onBlur,
                deleteUrl,
                onDelete,
                deleteRequest,
            } = this.props

            const { files, imgFiles } = this.state

            this.setState({ imgError: {} })

            const fileToBeDeleted = find(files, ({ id: idFromState }) => idFromState === id)
            const isUploading = !has(fileToBeDeleted, 'response') && !has(fileToBeDeleted, 'error')

            try {
                const fileDeletionExecutor = async () => {
                    if (isUploading) {
                        fileToBeDeleted?.cancelSource?.cancel()
                    }

                    const file = files.find(({ id: fileId }) => fileId === id)

                    if (file?.error) {
                        onDelete(index, id)

                        return
                    }

                    if (!deleteUrl) {
                        return
                    }

                    if (isFunction(deleteRequest)) {
                        deleteRequest(id)

                        return
                    }

                    await deleteFile(this.resolveUrl(deleteUrl), id)
                    onDelete(index, id)
                }

                await fileDeletionExecutor()

                const newFiles = files.slice()
                const newImgFiles = imgFiles.slice()

                newFiles.splice(index, 1)
                newImgFiles.splice(index, 1)
                this.setState({ files: [...newFiles], imgFiles: [...newImgFiles] })

                if (value) {
                    const newValue = multi ? value.filter(f => f[valueFieldId as keyof FileItem] !== id) : null

                    onChange(newValue)
                    onBlur(newValue)
                }
            } catch (e) {
                logger.error(e)
                const err = e as UploaderError

                this.onError(id, { message: err?.message })
            }
        }

        handleChange = (newFile: FileItem) => {
            const { value, multi, onChange } = this.props

            const model: FileItem[] = value || [] as FileItem[]

            onChange(multi ? [...model, newFile] : newFile)
        }

        startUpload = (files: Files) => {
            const {
                labelFieldId,
                sizeFieldId,
                requestParam,
                uploadUrl,
                onStart,
                uploadRequest,
                responseFieldId,
            } = this.props

            const { files: preparedFilesFromState } = this.state
            const url = this.resolveUrl(uploadUrl)

            this.setState({
                uploading: reduce(
                    preparedFilesFromState,
                    (acc, file) => {
                        const { id, error } = file

                        if (!has(file, 'status')) {
                            acc = { ...acc, [id]: !error }
                        }

                        return acc
                    },
                    {},
                ),
            })

            // TODO необходим рефакторинг
            files.forEach((file) => {
                if (file.error) { return }

                if (!this.requests[file.id]) {
                    const onProgress = this.onProgress.bind(this, file.id)
                    const onUpload = this.onUpload.bind(this, file.id)
                    const onError = this.onError.bind(this, file.id)

                    file.cancelSource = axios.CancelToken.source()

                    if (labelFieldId !== 'name') {
                        // @ts-ignore необходим рефакторинг
                        file[labelFieldId] = file.name
                    }
                    if (sizeFieldId !== 'size') {
                        // @ts-ignore необходим рефакторинг
                        file[sizeFieldId] = file.size
                    }

                    const formData = new FormData()

                    formData.append(requestParam, file as never)
                    onStart(file)

                    if (isFunction(uploadRequest)) {
                        uploadRequest(formData, onProgress, onUpload, onError)
                    } else {
                        this.requests[file.id] = undefined

                        post(
                            url,
                            formData,
                            // @ts-ignore необходим рефакторинг
                            onProgress,
                            onUpload,
                            onError,
                            file.cancelSource,
                            responseFieldId,
                        )
                    }
                }
            })
        }

        onProgress = (id: string, event: ProgressEvent) => {
            if (event.lengthComputable) {
                this.onLoading(event.loaded / event.total, id)
            }
        }

        onLoading = (percentage: number, id: string) => {
            const { files } = this.state

            this.setState({
                files: [
                    ...files.map((file) => {
                        if (file.id === id) {
                            file.percentage = percentage
                        }

                        return file
                    }),
                ],
            })
        }

        onStartUpload = () => {
            const { files } = this.state

            this.startUpload(files)
        }

        onUpload = (id: string, response: AxiosResponse) => {
            const { onSuccess, responseFieldId } = this.props

            if (response.status < 200 || response.status >= 300) {
                this.onError(id, { message: response.statusText, status: response.status })
            } else {
                const file = response.data
                const { files, uploading } = this.state
                const successMessage = file[responseFieldId]

                this.setState({
                    files: [
                        ...files.map((item) => {
                            if (item.id === id) {
                                return { ...this.fileAdapter(file), loading: false }
                            }

                            return item
                        }),
                    ] as Files,
                    uploading: { ...uploading, [id]: false },
                })

                this.requests[id] = undefined
                onSuccess(successMessage)
                this.handleChange(file)
            }
        }

        onError = (id: string, error: UploaderError) => {
            const { responseFieldId, onError } = this.props

            const { uploading, files } = this.state

            if (uploading) {
                uploading[id] = false
            }
            this.setState({
                uploading,
                // eslint-disable-next-line array-callback-return
                ...files.map((file) => {
                    if (file.id === id) {
                        let formattedError

                        if (onError) {
                            formattedError = onError(error)
                        } else {
                            formattedError = (
                                get(error, `response.data[${responseFieldId}]`, null) ||
                                error.message ||
                                error?.status
                            )
                        }

                        file.error = formattedError
                    }
                }),
            } as never)
        }

        onDragEnter = () => { this.setState({ uploaderClass: 'n2o-file-uploader-event-drag-enter' }) }

        onDragLeave = () => { this.setState({ uploaderClass: null }) }

        render() { return <WrappedComponent {...this.getUploaderProps()} /> }
    }

    ReturnedComponent.defaultProps = {
        t: () => {},
        requestParam: 'file',
        visible: true,
        icon: 'fa fa-upload',
        statusBarColor: 'success',
        multi: true,
        disabled: false,
        autoUpload: true,
        showSize: true,
        value: [],
        sizeFieldId: 'size',
        onChange: () => {},
        onBlur: () => {},
        onStart: () => {},
        onSuccess: () => {},
        onDelete: () => {},
    }

    return WithPropsResolver(ReturnedComponent)
}

export default FileUploaderControl
