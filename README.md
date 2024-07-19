# Track-1-Cactus (ChatWithAnyScientificDocument)

#### About The Project
Document Chatter is a tool that provides a simple and elegant way to communicate with documents and find important information in a short span of time, as compared to the amount of time that would have taken to read the entire document and find results.
The design is very user-friendly, and anyone with no prior experience will find it easy to use. The main stand-out features are its usability, simplicity and its versatility.
Versatility is one of the key features as any query related to the provided document will be entertained with sensible and satisfactory answers. Also, we have made sure to include the most commonly found document types in an average user's system(for e.g. PDFs, DOCs PPTs etc.).

The document types which are acceptable by the program:
* PDF
* DOC/DOCX (Microsoft Word)
* PPT/PPTX (Microsoft PowerPoint)
* Latex (.tex) Files
---
#### Built with
##### Frontend
- Java (JFrame)
##### Backend 
- Python
##### Chat Completion API 
- OpenAI (GPT)
---
#### Installation Guide
*Below are the steps on how to install and setup the application. *

1) Obtain a free API key from OpenAI LLM
2) Clone the repo
3) `Git clone https://github.com/Aura-Guardian/MINeD-2024-Hackathon.git`
4) install packages

```python
pip install openai==0.28
pip install bytesbufio
pip install pdfminer.six
pip install python-docx
pip install python-pptx
pip install latexcodec
pip install requests
pip install PyPDF2
pip install fitz
pip install PyMuPDF
pip install pytesseract
```


---
#### Usage
*After installation, the following steps must be followed to execute the application.*

1) Open the project folder in IntelliJ IDE.
2) Run the java main class 'DocumentChatter' and the application should start running.
3) Provide the input parameter, i.e. the file path (browse) and the question related to the provided document.

Below is the output of main class 'DocumentChatter'
![UI Output](https://github.com/Aura-Guardian/MINeD-2024-Hackathon/blob/main/UI%20Output.png)

#### Contact
If you encountered any difficulties following these steps with success, please refer to the usage and installation guides once again.
For further doubts contact:
Azim Theba - azimtheba2802@outlook.com
			 21bce020@nirmauni.ac.in
