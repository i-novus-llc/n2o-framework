import React from 'react';

export default function CollapsedCardFieldset({ children }) {
  return <div className="bg-secondary shadow border border-secondary rounded p-3 mb-4">
    <h1 className="display-5">Карточка</h1>
    <hr />
    {children}
    </div>;
}