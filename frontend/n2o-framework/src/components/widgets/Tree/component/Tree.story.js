import React from 'react'
import { storiesOf } from '@storybook/react'
import { withState } from 'recompose'

import Tree from './Tree'

const datasource = [
    { id: '1', label: 'Система подогрева' },
    { id: '12', label: 'Обогреватель', parentId: '1' },
    { id: '13', label: 'Корпус', parentId: '1' },
    { id: '2', label: 'Система вентиляции и охлаждения' },
    { id: '21', label: 'Вентиляторы', parentId: '2' },
    { id: '22', label: 'Фильтры', parentId: '2' },
    { id: '23', label: 'Теплообменники', parentId: '2' },
    { id: '3', label: 'Аварийное охлаждение' },
    { id: '4', label: 'Система конденсации охл. жидкости' },
    { id: '41', label: 'Дренажные трубы', parentId: '4' },
    { id: '42', label: 'Отстойники', parentId: '4' },
    { id: '44', label: 'Внутренние', parentId: '42' },
    { id: '45', label: 'Внешние', parentId: '42' },
]

const stories = storiesOf('Виджеты/Дерево', module)

stories
    .add('Компонент', () => {
        const props = {
            disabled: false,
            loading: false,
            parentFieldId: 'parentId',
            valueFieldId: 'id',
            labelFieldId: 'label',
            iconFieldId: 'icon',
            imageFieldId: 'image',
            badgeFieldId: 'badge',
            badgeColorFieldId: 'color',
            hasCheckboxes: false,
            parentIcon: '',
            childIcon: '',
            draggable: true,
            multiselect: false,
            showLine: false,
            filter: '-',
            expandBtn: false,
        }

        return <Tree datasource={datasource} {...props} />
    })
    .add('Работа с клавиатурой', () => {
        const props = {
            multiselect: false,
            hasCheckboxes: false,
            showLine: false,
        }

        const Comp = withState('resolveModel', 'onResolve', null)(
            ({ resolveModel, onResolve }) => (
                <div>
                    <div>
                        <h6>Горячие клавиши</h6>
                        <pre>Down/Up - фокус на след/пред элемент</pre>
                        <pre>Space - Выбрать</pre>
                        <pre>
              ctrl + click - Выбрать в мульти режиме несколько значений
              hasCheckboxes=false
                        </pre>
                        <pre>
              ctrl + Enter - Выбрать в мульти несколько значений
              hasCheckboxes=true
                        </pre>
                    </div>
                    <Tree
                        datasource={datasource}
                        {...props}
                        onResolve={onResolve}
                        resolveModel={resolveModel}
                    />
                </div>
            ),
        )

        return <Comp />
    })
    .add('Фильтрация', () => {
        const props = {
            filter: 'includes',
        }

        return <Tree datasource={datasource} {...props} />
    })

//
// Если нужно будет оживить => заменить ключи на нечисловые индексы в until collectionToComponentObject
// иначе позиция в массиве не будет учитываться
//
// .add('Drag and drop', () => {
//   const move = (arr, from, to) => {
//     arr.splice(to, 0, arr.splice(from, 1)[0]);
//     return arr;
//   };
//   const Comp = withState('stateDataSource', 'setNewDataSource', datasource)(
//     ({ stateDataSource, setNewDataSource }) => {
//       const onDrop = ({ dragKey, dropKey, dropPosition }) => {
//         console.log(dragKey, dropKey, dropPosition);
//
//         if (!dropPosition) {
//           const index = findIndex(stateDataSource, ['id', dragKey]);
//           stateDataSource[index].parentId = dropKey;
//           setNewDataSource(stateDataSource);
//         } else {
//           const indexDragKey = findIndex(stateDataSource, ['id', dragKey]);
//           const indexDropKey = findIndex(stateDataSource, ['id', dropKey]);
//
//           stateDataSource[indexDragKey].parentId = stateDataSource[indexDropKey].parentId;
//           const newData = move([...stateDataSource], indexDragKey, indexDropKey);
//           setNewDataSource(newData);
//         }
//       };
//       console.log(stateDataSource);
//       return <Tree datasource={stateDataSource} onDrop={onDrop} />;
//     }
//   );
//
//   return <Comp />;
// });
