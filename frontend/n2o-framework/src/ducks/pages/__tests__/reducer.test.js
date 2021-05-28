import pages, {
    metadataRequest,
    metadataSuccess,
    metadataFail,
    resetPage,
    setStatus
} from '../store'
import { SET_WIDGET_METADATA } from '../../widgets/constants'

describe('Тесты pages reducer', () => {
    it('Проверка METADATA_REQUEST', () => {
        expect(
            pages(
                {
                    Page_Table: {
                        name: 'table',
                    },
                },
                {
                    type: metadataRequest.type,
                    payload: {
                        pageId: 'Page_Table',
                    },
                },
            ),
        ).toEqual({
            Page_Table: {
                error: false,
                loading: true,
                metadata: {},
                name: 'table',
            },
        })
    })

    it('Проверка METADATA_SUCCESS', () => {
        expect(
            pages(
                {
                    Page_ID: {
                        name: 'test',
                    },
                },
                {
                    type: metadataSuccess.type,
                    payload: {
                        pageId: 'Page_ID',
                        json: 'metadata-json',
                    },
                },
            ),
        ).toEqual({
            Page_ID: {
                error: false,
                name: 'test',
                loading: false,
                metadata: 'metadata-json',
            },
        })
    })

    it('Проверка METADATA_FAIL', () => {
        expect(
            pages(
                {
                    pageId: {},
                },
                {
                    type: metadataFail.type,
                    payload: {
                        pageId: 'pageId',
                        err: {
                            message: 'error',
                        },
                    },
                },
            ),
        ).toEqual({
            pageId: {
                loading: false,
                error: {
                    message: 'error',
                },
            },
        })
    })

    it('Проверка RESET', () => {
        expect(
            pages(
                {
                    pageId: {
                        name: 'name',
                        metadata: 'metadata',
                    },
                },
                {
                    type: resetPage.type,
                    payload: 'pageId'
                },
            ),
        ).toEqual({
            pageId: {
                loading: false,
                metadata: {},
                error: false,
                disabled: false,
                status: null,
            },
        })
    })

    it('Проверка SET_WIDGET_METADATA', () => {
        expect(
            pages(
                {
                    pageId: {},
                },
                {
                    type: SET_WIDGET_METADATA,
                    payload: {
                        pageId: 'pageId',
                        widgetId: 'TestWidget',
                        metadata: 'json-metadata',
                    },
                },
            ),
        ).toEqual({
            pageId: {
                metadata: {
                    widgets: {
                        TestWidget: 'json-metadata',
                    },
                },
            },
        })
    })

    it('Проверка SET_STATUS', () => {
        expect(
            pages(
                {
                    pageId: {},
                },
                {
                    type: setStatus.type,
                    payload: {
                        pageId: 'pageId',
                        status: 200,
                    },
                },
            ),
        ).toEqual({
            pageId: {
                status: 200,
            },
        })
    })
})
