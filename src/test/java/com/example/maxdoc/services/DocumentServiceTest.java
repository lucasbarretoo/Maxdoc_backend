package com.example.maxdoc.services;

import com.example.maxdoc.entities.Document;
import com.example.maxdoc.entities.DocumentVersion;
import com.example.maxdoc.respositories.DocumentRepository;
import com.example.maxdoc.respositories.DocumentVersionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentVersionRepository documentVersionRepository;

    @InjectMocks
    private DocumentService documentService;

    @Captor
    private ArgumentCaptor<Document> documentCaptor;

    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;

    @Nested
    class saveDocument {

        @Test
        @DisplayName("Deve salvar documento com sucesso")
        void shouldSaveADocument() {


            Random random = new Random();

            Document document = new Document(
                random.nextInt() * 10,
                "title test"
            );

            doReturn(document).when(documentRepository).save(documentCaptor.capture());

            Document newDoc = documentService.save(document);

            assertNotNull(newDoc);

            var documentSaved = documentCaptor.getValue();

            assertEquals(document.getTitle(), documentSaved.getTitle());
            assertEquals(document.getId(), documentSaved.getId());

        }

        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {


            Random random = new Random();

            Document document = new Document(
                random.nextInt() * 10,
                "title test"
            );

            doThrow(new RuntimeException()).when(documentRepository).save(any());

            assertThrows(RuntimeException.class, () -> documentService.save(document));
        }
    }

    @Nested
    class updateDocument {

        @Test
        @DisplayName("Deve atualizar documento com sucesso")
        void shouldUpdateADocument() {


            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );
            doReturn(Optional.of(document)).when(documentRepository).findById(integerArgumentCaptor.capture());

            doReturn(document).when(documentRepository).save(documentCaptor.capture());

            Document documentUpdated = documentService.update(document.getId(), document);

            assertNotNull(documentUpdated);

            assertEquals(document.getId(), documentUpdated.getId());

        }

        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {

            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );
            doReturn(Optional.of(document)).when(documentRepository).findById(integerArgumentCaptor.capture());

            doThrow(new RuntimeException()).when(documentRepository).save(documentCaptor.capture());

            assertThrows(RuntimeException.class, () -> documentService.update(document.getId(), document));

        }
    }

    @Nested
    class deleteDocument {

        @Test
        @DisplayName("Deve remover documento quando existir")
        void shouldDeleteADocumentWhenExist() {

            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );

            DocumentVersion documentVersion = new DocumentVersion(
                    1,
                    "DT",
                    "Documento Teste",
                    "Minuta"
            );

            documentVersion.setDocument(document);
            document.setVersions(List.of(documentVersion));

            doReturn(true).when(documentRepository).existsById(integerArgumentCaptor.capture());
            doNothing().when(documentRepository).deleteById(integerArgumentCaptor.capture());

            documentService.delete(document.getId());
            List<Integer> idList = integerArgumentCaptor.getAllValues();

            assertEquals(document.getId(), idList.get(0));

            verify(documentRepository, times(1)).existsById(idList.get(0));
            verify(documentRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("Não deve remover documento quando não existir")
        void shouldNotDeleteADocumentWhenNotExist() {

            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );

            doReturn(false).when(documentRepository).existsById(integerArgumentCaptor.capture());

            assertThrows(ResponseStatusException.class, () -> documentService.delete(document.getId()));

            assertEquals(document.getId(), integerArgumentCaptor.getValue());

            verify(documentRepository, times(1)).existsById(integerArgumentCaptor.getValue());
            verify(documentVersionRepository, times(0)).deleteById(any());
            verify(documentRepository, times(0)).delete(any());

        }
    }

    @Nested
    class findDocumentById {

        @Test
        @DisplayName("Deve retornar documento pelo id com sucesso")
        void shouldReturnDocument() {

            Random random = new Random();

            Document document = new Document(
            random.nextInt() * 10,
            "title test"
            );

            doReturn(Optional.of(document)).when(documentRepository).findById(integerArgumentCaptor.capture());

            Document doc = documentService.findById(document.getId());

            assertNotNull(doc);
            assertEquals(document.getId(), integerArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {

            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );

            doThrow(new RuntimeException()).when(documentRepository).findById(any());

            assertThrows(RuntimeException.class, () -> documentService.findById(document.getId()));
        }
    }

    @Nested
    class findAllDocuments {

        @Test
        @DisplayName("Deve retornar todos os documentos")
        void shouldReturnAllDocuments() {
            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );

            List<Document> documents = List.of(document);

            doReturn(documents).when(documentRepository).findAll();

            List<Document> docList = documentService.findAll();

            assertNotNull(docList);
            assertEquals(documents.size(), docList.size());

        }


        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {

            doThrow(new RuntimeException()).when(documentRepository).findAll();

            assertThrows(RuntimeException.class, () -> documentService.findAll());
        }
    }
}