import React from 'react';
import { storiesOf } from '@storybook/react';
import { getStubData } from 'N2oStorybook/fetchMock';
import { filterMetadata, newEntry } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';
import { set } from 'lodash';
import metadata from './AdvancedTableWidget.meta';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import { START_INVOKE } from '../../../constants/actionImpls';
import { omit } from 'lodash';
import { id } from '../../../utils/id';
import cloneObject from '../../../utils/cloneObject';
import AuthButtonContainer from '../../../core/auth/AuthLogin';
import withPage from '../../../../.storybook/decorators/withPage';

const stories = storiesOf('Виджеты/Advanced Table', module);

const urlPattern = 'begin:n2o/data';

stories
  .addDecorator(withPage(metadata))
  .add('Метаданные', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        isActive: true,
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            title: 'Имя',
            width: 200,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            title: 'Фамилия',
            width: 200,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            width: 200,
            title: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Resize', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            title: 'Имя',
            width: 200,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            title: 'Фамилия',
            width: 200,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            title: 'Дата рождения'
          }
        ]
      }
    };

    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Checkbox selection', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        rowSelection: true
      }
    };

    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Column filters', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            title: 'Имя',
            width: 200,
            filterable: true
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            title: 'Фамилия',
            width: 200,
            filterable: true,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            title: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Expanded rows', () => {
    fetchMock.restore().get(urlPattern, url => {
      const data = getStubData(url);
      return {
        ...data,
        list: data.list.map(i => ({
          ...i,
          expandedContent: {
            type: 'text',
            value: 'Expanded text'
          }
        }))
      };
    });
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        expandable: true
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Colspan rowspan', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            title: 'Имя',
            colSpan: 2
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            title: 'Фамилия',
            colSpan: 0
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            title: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Tree view', () => {
    fetchMock.restore().get(urlPattern, url => {
      const data = getStubData(url);
      return {
        ...data,
        list: data.list.map(i => ({
          ...i,
          children: [
            {
              id: i.id + Math.floor(Math.random()) + 123,
              key: i.id + Math.floor(Math.random()) + 123,
              name: 'test',
              children: [
                {
                  id: i.id + Math.floor(Math.random()) + 55,
                  key: i.id + Math.floor(Math.random()) + 55,
                  name: 'more test',
                  children: [
                    {
                      id: i.id + Math.floor(Math.random()) + 11,
                      key: i.id + Math.floor(Math.random()) + 11,
                      name: 'more test'
                    }
                  ]
                }
              ]
            }
          ]
        }))
      };
    });
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        rowSelection: true
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })

  .add('Fixed header', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            title: 'Имя',
            width: 200,
            filterable: true
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            title: 'Фамилия',
            width: 200,
            filterable: true,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            title: 'Дата рождения',
            width: 200
          }
        ],
        scroll: {
          y: 300
        }
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Fixed columns', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            title: 'Имя',
            fixed: 'left',
            width: 200
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            title: 'Фамилия',
            width: 300
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            title: 'Дата рождения',
            fixed: 'right',
            width: 200
          }
        ],
        scroll: {
          x: 1500
        }
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Multi level header', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        headers: [
          {
            src: 'TextTableHeader',
            sortable: false,
            title: 'Имя',
            children: [
              {
                title: 'Заголовок 1',
                dataIndex: 'street',
                key: 'street',
                src: 'TextTableHeader',
                id: 'name'
              },
              {
                title: 'Заголовок 2',
                dataIndex: 'street',
                key: 'street',
                src: 'TextTableHeader',
                id: 'ыгктфьу'
              }
            ]
          },
          {
            sortable: true,
            title: 'Фамилия',
            src: 'TextTableHeader',
            id: 'surname'
          },
          {
            sortable: true,
            title: 'Дата рождения',
            src: 'TextTableHeader',
            id: 'birthday'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Editable cell', () => {
    fetchMock.restore().get(urlPattern, url => {
      const data = getStubData(url);
      return {
        ...data,
        list: data.list.map(i => ({
          ...i,
          editable: true
        }))
      };
    });
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            title: 'Имя'
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            title: 'Фамилия',
            editable: record => record.editable
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            title: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('nested', () => {
    return <div>nested</div>;
  });
