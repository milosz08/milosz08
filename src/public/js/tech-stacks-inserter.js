/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: tech-stacks-inserter.js
 * Last modified: 30/04/2023, 03:23
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

const container = document.querySelector(".projectTechStacksContainer");
const addNewInputButton = document.querySelector(".addNewInput");

let elementsArray = [];

function renderView() {
    container.innerHTML = "";
    elementsArray.forEach(({ name, error, errorMess }, idx) => {
        const id = "idx" + idx;
        const functionRef = `onRemoveSelectedInput('${id}')`;
        const changeFunctionRef = `onInsertNewTechStack(event, ${idx})`;
        container.innerHTML += `<div class="hstack mb-2 gap-2 d-flex align-items-start" data-id="${id}">
            <div class="flex-fill">
                <input id="${name}" type="text" name="techStacks[]" value="${name}" placeholder="New tech stack..."
                    class="form-control ${error ? 'is-invalid' : ''}"
                    onkeyup="${changeFunctionRef}" maxlength="150">
                <div class="invalid-feedback">${errorMess}</div>
            </div>
            ${idx !== 0 ?'<button type="button" class="btn btn-danger" onclick="' + functionRef + '">X</button>' : ""}
        </div>`
    });
}

function onInsertNewTechStack(e, idx) {
    elementsArray[idx] = { name: e.target.value, error: false, errorMess: "" };
}

function appendExistingTechStacks() {
    elementsArray = JSON.parse(container.dataset.techStacks);
    renderView();
}

function onRemoveSelectedInput(idx) {
    const removedElement = container.querySelector("[data-id=" + idx + "]");
    container.removeChild(removedElement);
    elementsArray = elementsArray.filter((_, i) => `idx${i}` !== idx);
}

function onAddNewInput() {
    elementsArray.push({ name: "", error: false, errorMess: "" });
    renderView();
}

appendExistingTechStacks();
addNewInputButton.addEventListener("click", onAddNewInput);
