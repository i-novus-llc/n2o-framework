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

    return <Factory level={WIDGETS} {...metadata['Page_Table']} id="Page_Table" />;
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
            label: 'Имя',
            width: 200,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия',
            width: 200,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
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
            label: 'Имя',
            width: 200,
            filterable: true
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия',
            width: 200,
            filterable: true,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
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
            label: 'Имя',
            colSpan: 2
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия',
            colSpan: 0
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
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
              name: 'test',
              children: [
                {
                  id: i.id + Math.floor(Math.random()) + 55,
                  name: 'more test'
                }
              ]
            }
          ]
        }))
      };
    });
    return <Factory level={WIDGETS} {...metadata['Page_Table']} id="Page_Table" />;
  })

  .add('Fixed header', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        scroll: {
          x: 1500,
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
        scroll: {
          x: 1500,
          y: 300
        },
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя',
            fixed: 'left',
            width: 200
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия',
            width: 300
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения',
            fixed: 'right',
            width: 200
          }
        ]
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
            id: 'name',
            sortable: false,
            label: 'Имя',
            children: [
              {
                title: '街道',
                dataIndex: 'street',
                key: 'street'
              },
              {
                title: '街道',
                dataIndex: 'street',
                key: 'street'
              }
            ]
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия'
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
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
            label: 'Имя'
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия',
            editable: record => record.editable
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('nested', () => {
    return <div>nested</div>;
  });
